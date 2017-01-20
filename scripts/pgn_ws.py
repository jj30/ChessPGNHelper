import cherrypy
import re

import pymongo
from pymongo import MongoClient
from bson.json_util import dumps

class GetOptions(object):
    @cherrypy.expose
    def find_player(self, w, b):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'

        regex_w = re.compile(w, re.IGNORECASE)
        regex_b = re.compile(b, re.IGNORECASE)

        conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns")
        cur = conn.pgns.allPGNs

        all_results = cur.find({ "White" : { "$regex" : regex_w }, "Black" : { "$regex" : regex_b } }, { '_id' : False }).sort("White", pymongo.ASCENDING)
        # all_results = cur.find({}, {'_id': False}).limit(1)

        final_dct = {}
        final_dct["data"] = all_results

        return dumps(final_dct)

if __name__ == '__main__':
    cherrypy.config.update({'log.screen': True,
                        'log.access_file': 'ax.log',
                        'log.error_file': 'err.log'})
    cherrypy.config.update({'server.socket_host': '0.0.0.0', 'server.socket_port': 8080, })
    cherrypy.quickstart(GetOptions())
