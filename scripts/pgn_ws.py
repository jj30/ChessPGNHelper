import cherrypy
import sqlite3
import simplejson as json

class GetOptions(object):
    @cherrypy.expose
    def find_player(self, search):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'

        top_players = [ { '_id': 48496, 'Name' : 'Magnus Carlsen' },
                        {'_id': 20740, 'Name': 'Garry Kasparov'},
                        {'_id': 12377, 'Name': 'Bobby Fischer'},
                        {'_id': 1160, 'Name': 'Viswanathan Anand'},
                        {'_id': 20651, 'Name': 'Anatoly Karpov'},
                        {'_id': 22835, 'Name': 'Vladimir Kramnik'},
                        {'_id': 747, 'Name': 'Alexander Alekhine'},
                        {'_id': 6469, 'Name': 'Jose Raul Capablanca'},
                        {'_id': 5277, 'Name': 'Mikhail Botvinnik'},
                        {'_id': 40783, 'Name': 'Boris Spassky'},
                        {'_id': 29170, 'Name': 'Paul Morphy'},
                        {'_id': 24015, 'Name': 'Emmanuel Lasker'},
                        {'_id': 43333, 'Name': 'Veselin Topalov'},
                        {'_id': 32911, 'Name': 'Tigran Petrosian'},
                        {'_id': 41248, 'Name': 'Willhelm Steinitz'},
                        {'_id': 42289, 'Name': 'Mikhail Tal'},
                        {'_id': 1830, 'Name': 'Levoran Aronian'},
                        {'_id': 33690, 'Name': 'Judit Polgar'},
                        {'_id': 22287, 'Name': 'Viktor Korchnoi'},
                        {'_id': 40310, 'Name': 'Vasily Smyslov'},
                        {'_id': 11542, 'Name': 'Max Euwe'},
                        {'_id': 29844, 'Name': 'Hiraku Nakamura'},
                        {'_id': 6733, 'Name': 'Fabiano Caruana'},
                        {'_id': 42425, 'Name': 'Siegbert Tarrasch'},
                        {'_id': 30646, 'Name': 'Aron Nimzowitsch'},
                        {'_id': 18899, 'Name':  'Vassily Ivanchuk'},
                        {'_id': 9327, 'Name': 'Deep Blue'} ]

        final_json = []

        for dct in top_players:
            target = dct["Name"].upper()
            search = search.upper()

            if (search in target):
                final_json.append(dct)

        if (final_json == []):
            final_json = self.find_player_in_db(search)

        return json.dumps(final_json)

    @cherrypy.expose
    def find_games_of_player(self, player_id):
        browserOk = False
        browserOk = cherrypy.request.headers["User-Agent"] == 'okhttp/3.3.0'

        player_id = player_id.split("=")[1]
        params_w = None, None, None, None, player_id, None, None, None, None, None, None
        params_b = None, None, None, None, None, player_id, None, None, None, None, None

        final_json_w = self.find_game_in_db(params_w)
        final_json_b = self.find_game_in_db(params_b)

        final_jsonw = final_json_w + final_json_b
        return json.dumps(final_json_w)

    @cherrypy.expose
    def add_new_game(self):
        # Admin only
        print("if the password checks out, add the game to the database")

    def find_player_in_db(self, search):
        search = search.lower()
        bOk = not("update " in search)
        bOk = bOk and (not("delete " in search))
        bOk = bOk and (not("select " in search))
        bOk = bOk and (not("insert " in search))

        if (bOk):
            # returns json, not the dumps
            final_json = []
            conn = sqlite3.connect('PGNSDB2017')
            conn.row_factory = self.dict_factory
            cur = conn.cursor()

            sql = "SELECT DISTINCT IfNull(P2._id,  Players._id) as _id, " \
                "IfNull(P2.Name,  Players.Name) as Name, " \
                "IfNull(P2.OfficialID, Players.OfficialID) as OfficialID " \
                "FROM Players " \
                "LEFT JOIN Players P2 on Players.OfficialID = P2._id " \
                "WHERE Players. Name like '%{0}%';".format(search)

            results = cur.execute(sql)
            to_be_json = results.fetchall()

        conn.close()
        return to_be_json

    def dict_factory(self, cursor, row):
        d = {}
        for idx, col in enumerate(cursor.description):
            if (row[idx] != None):
                d[col[0]] = row[idx]
        return d

    def find_game_in_db(self, params):
        bOk = False
        event, site, date, round, white_id, black_id, result, white_elo, black_elo, eco, pgn = params

        if (black_id != None):
            sql = "SELECT * FROM GamesBlack WHERE BlackID = {0};".format(black_id)

            search = black_id.lower()
            bOk = not ("update " in search)
            bOk = bOk and (not ("delete " in search))
            bOk = bOk and (not ("select " in search))
            bOk = bOk and (not ("insert " in search))

        if (white_id != None):
            sql = "SELECT * FROM GamesWhite WHERE WhiteID = {0};".format(white_id)

            search = white_id.lower()
            bOk = not ("update " in search)
            bOk = bOk and (not ("delete " in search))
            bOk = bOk and (not ("select " in search))
            bOk = bOk and (not ("insert " in search))

        if (bOk):
            conn = sqlite3.connect('PGNSDB2017')
            conn.row_factory = self.dict_factory
            cur = conn.cursor()

            results = cur.execute(sql)

            to_be_json = results.fetchall()

        conn.close()
        return to_be_json

if __name__ == '__main__':
    cherrypy.config.update({'log.screen': True,
                        'log.access_file': 'ax.log',
                        'log.error_file': 'err.log'})
    cherrypy.config.update({'server.socket_host': '0.0.0.0', 'server.socket_port': 8080, })
    cherrypy.quickstart(GetOptions())
