import os
# import sqlite3
from pymongo import MongoClient

def allInFolder(folder, cursor):
    # absolute paths only, pls
    for obj in os.listdir(folder):
        sub_obj = folder + obj
        if (os.path.isdir(sub_obj)):
            allInFolder(sub_obj + "/", cursor)
        else:
            perFile(sub_obj, cursor)

def perFile(fileName, cur):
    class Game:
        Event = ""
        Site = ""
        Date = ""
        EventDate = ""
        Round = ""
        White = ""
        Black = ""
        Result = ""
        WhiteELO = ""
        BlackELO = ""
        ECO = ""
        PGN = ""
        PlyCount = ""

        '''def save_players(self, cur):
            for player in (self.White, self.Black):
                player = player.encode("ascii", "ignore")

                sql_lookup = "SELECT 1 FROM PlayersUnsorted WHERE Name = '{0}'".format(player)
                cur.execute(sql_lookup)

                if (cur.rowcount == -1):
                    sql_add = "INSERT INTO PlayersUnsorted(Name) VALUES( '{0}' );".format(player)
                    cur.execute(sql_add)

        def get_id(self, name):
            id = 0
            try:
                sql = "SELECT IfNull(OfficialID, _id) FROM Players WHERE Name = '{0}';".format(name)
                cur.execute(sql)

                (id, ) = cur.fetchone()
            except TypeError as e:
                print "TypeError {0}".format(sql)
                pass
            return id'''

        def questionMarksOnly(self, string):
            return string.replace("?", "") == ""

        def save(self, cur):
            # white_player_id = self.get_id(self.White)
            # black_player_id = self.get_id(self.Black)

            info = (self.Event, self.Site, self.Date, self.Round, self.White, self.Black, self.Result, self.WhiteELO, self.BlackELO, self.ECO, self.PGN.strip())
            headers = ("Event", "Site", "Date", "Round", "White", "Black", "Result", "WhiteELO", "BlackELO", "ECO", "PGN")

            try:
                # sql = "INSERT INTO GamesUnsorted (Event, Site, Date, Round, WhiteID, BlackID, Result, WhiteELO, BlackELO, ECO, PGN) VALUES {0}".format(info)
                # cur.execute(sql)

                pgn_record = {}

                for hdr in headers:
                    idx = headers.index(hdr)
                    if info[idx] != None and not(self.questionMarksOnly(info[idx])):
                        pgn_record[hdr] = info[idx]

                cur.insert(pgn_record)

            except:
                # print "Sql: {0}".format(sql)
                pass

        def complete(self):
            bIsComplete = self.Event != "" and  self.Site != "" and  self.Date != "" and \
                self.White != "" and self.Black != "" and self.Result != "" and \
                self.PGN != ""

            return bIsComplete

        def toString(self):
            info = (self.Event, self.Site, self.Date, self.Round, self.White, self.Black, self.Result, self.WhiteELO, self.BlackELO, self.ECO, self.PGN.strip())
            return "Event, Site, Date, Round, WhiteID, BlackID, Result, WhiteELO, BlackELO, ECO, PGN:::{0}".format(info)

    # new game object
    this_game = Game()

    with open(fileName, "r") as FILE:
        for line in FILE:
            line_upper = line.upper()

            if (line_upper.find("[EVENT") > -1 and line_upper.find("[EVENTDATE") == -1):
                # this code is duplicated because some
                # files dont have a line spacing before the next game.
                # which means this Event belongs to a new game
                bIsComplete = this_game.complete()
                if bIsComplete:
                    try:
                        this_game.save(cur)
                        this_game = Game()
                    except:
                        print this_game.toString()
                        pass
                else:
                    this_game.Event = getValue(line)

                bIsComplete = False

            elif line_upper.find("[SITE") > -1:
                this_game.Site = getValue(line)
            elif line_upper.find("[DATE") > -1:
                this_game.Date = getValue(line)
            elif line_upper.find("[EVENTDATE") > -1:
                this_game.EventDate = getValue(line)
            elif line_upper.find("[ROUND") > -1:
                this_game.Round = getValue(line)
            elif line_upper.find("[WHITE") > -1 and line_upper.find("[WHITEELO") == -1:
                this_game.White = getValue(line)
            elif line_upper.find("[BLACK") > -1 and line_upper.find("[BLACKELO") == -1:
                this_game.Black = getValue(line)
            elif line_upper.find("[RESULT") > -1:
                this_game.Result = getValue(line)
            elif line_upper.find("[WHITEELO") > -1:
                this_game.WhiteELO = getValue(line)
            elif line_upper.find("[BLACKELO") > -1:
                this_game.BlackELO = getValue(line)
            elif line_upper.find("[ECO") > -1:
                this_game.ECO = getValue(line)
            elif line_upper.find("[PLYCOUNT") > -1:
                this_game.PlyCount = getValue(line)
            elif line_upper.find("[") > -1:
                # curve balls bc there is no standard set of tags for these files
                # so if there is [RandomNewTag], the line will be ignored and
                # not added to the pgns
                pass
            else:
                bIsComplete = this_game.complete()
                if (line == "\n") and bIsComplete:
                    try:
                        this_game.save(cur)
                        this_game = Game()
                    except:
                        print this_game.toString()
                        pass
                else:
                    this_game.PGN += " " + line.rstrip("\n\r")

                bIsComplete = False

def getValue(line):
    sTemp = line.strip();
    # the meat is between the double quotes
    sTemp = sTemp[sTemp.find("\"") + 1: sTemp.rfind("\"")]
    # for the o'connors
    sTemp = sTemp.replace("'", "''")
    sTemp = sTemp.replace("\\", "")

    return sTemp

if __name__ == '__main__':
    # conn = sqlite3.connect('/home/jj/Code/ImportPGNs2017/PGNSDB2017')
    conn = MongoClient("mongodb://:27017/")
    # cur = conn.cursor()
    cur = conn.pgns.allPGNs

    # trailing backslash important
    # pgn_folder = "./AllPGNs/"
    pgn_folder = "./AllPGNs/"
    allInFolder(pgn_folder, cur)

    # conn.commit()
