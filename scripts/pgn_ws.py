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

            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns_new")
            cur = conn.pgns_new.allPGNs

            if (ic == u'0'):
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
            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns_new")
            cur = conn.pgns_new.allPGNs

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

            conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns_new")
            cur = conn.pgns_new.allPGNs

            all_results = cur.find({ "White" : { "$regex" : regex_w }, "Black" : { "$regex" : regex_b } }, { "Black" : True, "_id" : False }).distinct("Black")
        else:
            return "Access Denied."

        return dumps(all_results)

    @cherrypy.expose
    def more_notes(self):
        str_return = '<!DOCTYPE html> \
            <html>\
            <head>\
               <meta http-equiv="refresh" content="0; url=http://chesspgnhelper.blogspot.com/">\
            </head>\
            <body>\
               <p>The page has moved to:\
               <a href="http://chesspgnhelper.blogspot.com/">this page</a></p>\
            </body>\
            </html>'

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
