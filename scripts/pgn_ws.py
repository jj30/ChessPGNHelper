import cherrypy
import re

import pymongo
from pymongo import MongoClient
from bson.json_util import dumps

class GetOptions(object):
    @cherrypy.expose
    def find_player(self, w, b):
        return self.find_player_wic(w, b, 0)

    @cherrypy.expose
    def find_player_wic(self, w, b, ic):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'
        browserOk = browserOk or (cherrypy.request.headers['Remote-Addr'] == '73.1.8.152')

        if (browserOk):
            regex_w = re.compile(w, re.IGNORECASE)
            regex_b = re.compile(b, re.IGNORECASE)

            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns")
            cur = conn.pgns.allPGNs

            if (ic == 0):
                all_results = cur.find({ "White" : { "$regex" : regex_w }, "Black" : { "$regex" : regex_b } }, { '_id' : False }).limit(100).sort("White", pymongo.ASCENDING)
            else:
                all_results = cur.find({ "White" : { "$in" : [regex_w, regex_b] }, "Black" : { "$in" : [regex_w, regex_b] }}, { '_id' : False }).limit(100).sort("White", pymongo.ASCENDING)

            final_dct = {}
            final_dct["data"] = all_results

            return dumps(final_dct)

        else:
            return "Access Denied."

    @cherrypy.expose
    def auto_white(self, w):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'
        browserOk = browserOk or (cherrypy.request.headers['Remote-Addr'] == '73.1.8.152')

        all_results = []
        if (browserOk):
            regex_w = re.compile(w, re.IGNORECASE)
            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns")
            cur = conn.pgns.allPGNs

            all_results = cur.find({ "White" : { "$regex" : regex_w } }, { 'White' : True, '_id' : False }).distinct("White")
        else:
            return "Access Denied."

        return dumps(all_results)

    @cherrypy.expose
    def auto_black(self, w, b):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'
        browserOk = browserOk or (cherrypy.request.headers['Remote-Addr'] == '73.1.8.152')

        all_results = []
        if (browserOk):
            regex_w = re.compile(w, re.IGNORECASE)
            regex_b = re.compile(b, re.IGNORECASE)

            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns")
            cur = conn.pgns.allPGNs

            all_results = cur.find({ "White" : { "$regex" : regex_w }, "Black" : { "$regex" : regex_b } }, { "Black" : True, "_id" : False }).distinct("Black")
        else:
            return "Access Denied."

        return dumps(all_results)

    @cherrypy.expose
    def more_notes(self):
        str_return = '<html><head><title>More about Chess PGN Helper</title></head><body>1/28/17.<P>This is more information \
            about the PGN Helper. At current writing, we have about 288K games, some of which are duplicates. \
            We are always working to tidy up our data. More features will follow over the coming months. If you want to share \
            an interesting PGN, or if you want to request a feature in the application -- \
            <a href="mailto:janjansz90210@gmail.com?subject=Chess PGN Helper">send it to me.</a><P>Latest tweaks: auto-complete. \
            For the white player, auto-complete is unrestricted; for the black one, the players are limited to the ones that had \
            matches with the white player. This means, conceivably, that if you pick "Bobby Fischer" and not "Robert Fischer", \
            you may not find the game you\'re looking for; if you feel this is the case, do not pick an option from the auto-complete \
            suggestions, and run the search with three or four characters in each player\'s name, eg. \'Fisc\'. <P>\
            Please be patient, as there are 26K+ players.</body></html>'

        return str_return

    @cherrypy.expose
    def log_err(self, err):
        cherrypy.log("FROM APP: " + err)


if __name__ == '__main__':
    cherrypy.config.update({'log.screen': True,
                        'log.access_file': 'ax.log',
                        'log.error_file': 'err.log'})
    cherrypy.config.update({'server.socket_host': '0.0.0.0', 'server.socket_port': 8080, })
    cherrypy.quickstart(GetOptions())
