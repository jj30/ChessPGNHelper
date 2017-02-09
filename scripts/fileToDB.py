import os
import sys
import traceback
from pymongo import MongoClient
import sqlite3

def allInFolder(folder, cursor, s_conn, s_cur):
    # absolute paths only, pls
    all_in_folder = sorted(os.listdir(folder))

    for obj in all_in_folder:
        sub_obj = folder + obj
        if (os.path.isdir(sub_obj)):
            allInFolder(sub_obj + "/", cursor, s_conn, s_cur)
        else:
            perFile(sub_obj, cursor, s_conn, s_cur)
            print "Completed {0}".format(sub_obj)

def perFile(fileName, cur, s_conn, s_cur):
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

        '''

        def questionMarksOnly(self, string):
            return string.replace("?", "") == ""

        def get_normalized(self, name):
            if "'" in name:
                name = name.replace("'", "''")

            try:
                sql = "select ifnull(p2.name, p.name) from player p left join player p2 on p.official_id = p2._id where p.name = '{0}';".format(name)
                s_cur.execute(sql)

                (normalized,) = s_cur.fetchone()
            except TypeError as e:
                return name
                print "TypeError {0}".format(sql)
                pass

            return normalized

        def save(self, cur):
            white_player_norm = self.get_normalized(self.White)
            black_player_norm = self.get_normalized(self.Black)

            info = (self.Event, self.Site, self.Date, self.Round, white_player_norm, black_player_norm, self.Result, self.WhiteELO, self.BlackELO, self.ECO, self.PGN.strip())
            headers = ("Event", "Site", "Date", "Round", "White", "Black", "Result", "WhiteELO", "BlackELO", "ECO", "PGN")

            try:
                # sql = "INSERT INTO GamesUnsorted (Event, Site, Date, Round, WhiteID, BlackID, Result, WhiteELO, BlackELO, ECO, PGN) VALUES {0}".format(info)
                # cur.execute(sql)

                pgn_record = {}

                for hdr in headers:
                    idx = headers.index(hdr)
                    if info[idx] != None and not(self.questionMarksOnly(info[idx])):
                        pgn_record[hdr] = info[idx]

                # results = cur.find(pgn_record)
                results = cur.find({ "PGN" : pgn_record["PGN"] }).limit(1)
                results_count = results.count(True)
                if (results_count == 0):
                    cur.insert(pgn_record)
                else:
                    list_results = list(results)
                    # print "White {0} vs. Black {1} played PGN: {2}".format(pgn_record["White"], pgn_record["Black"], pgn_record["PGN"])
                    print "From Cloud: White {0} vs. Black {1} played PGN: {2}".format(list_results[0]["White"], list_results[0]["Black"], list_results[0]["PGN"])


            except:
                traceback.print_exc(file=sys.stdout)
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

            # if (line_upper.find("[EVENT") > -1 and line_upper.find("[EVENTDATE") == -1):
            if ("[EVENT" in line_upper and not("[EVENTDATE") in line_upper):
                # this code is duplicated because some
                # files dont have a line spacing before the next game.
                # which means this Event belongs to a new game
                bIsComplete = this_game.complete()
                if bIsComplete:
                    try:
                        this_game.save(cur)
                        this_game = Game()
                    except:
                        # print this_game.toString()
                        traceback.print_exc(file=sys.stdout)
                        pass

                this_game.Event = getValue(line)
                bIsComplete = False

            # elif line_upper.find("[SITE") > -1:
            elif "[SITE" in line_upper:
                this_game.Site = getValue(line)
            # elif line_upper.find("[DATE") > -1:
            elif "[DATE" in line_upper:
                this_game.Date = getValue(line)
            elif "[EVENTDATE" in line_upper:
                this_game.EventDate = getValue(line)
            # elif line_upper.find("[ROUND") > -1:
            elif "[ROUND" in line_upper:
                this_game.Round = getValue(line)
            # elif line_upper.find("[WHITE") > -1 and line_upper.find("[WHITEELO") == -1:
            elif "[WHITE" in line_upper and not("[WHITEELO" in line_upper):
                this_game.White = getValue(line)
            # elif line_upper.find("[BLACK") > -1 and line_upper.find("[BLACKELO") == -1:
            elif "[BLACK" in line_upper and not("[BLACKELO" in line_upper):
                this_game.Black = getValue(line)
            # elif line_upper.find("[RESULT") > -1:
            elif "[RESULT" in line_upper:
                this_game.Result = getValue(line)
            # elif line_upper.find("[WHITEELO") > -1:
            elif "[WHITEELO" in line_upper:
                this_game.WhiteELO = getValue(line)
            # elif line_upper.find("[BLACKELO") > -1:
            elif "[BLACKELO" in line_upper:
                this_game.BlackELO = getValue(line)
            # elif line_upper.find("[ECO") > -1:
            elif "[ECO" in line_upper:
                this_game.ECO = getValue(line)
            # elif line_upper.find("[PLYCOUNT") > -1:
            elif "[PLYCOUNT" in line_upper:
                this_game.PlyCount = getValue(line)
            elif line_upper[0] == "[":
                # curve balls bc there is no standard set of tags for these files
                # so if there is [RandomNewTag], the line will be ignored and
                # not added to the pgns
                pass
            else:
                this_game.PGN += " " + line.rstrip("\n\r")

        # save the last game in the file.
        this_game.save(cur)

def getValue(line):
    sTemp = line.strip();
    # the meat is between the double quotes
    sTemp = sTemp[sTemp.find("\"") + 1: sTemp.rfind("\"")]
    sTemp = sTemp.replace("\\", "")

    return sTemp

if __name__ == '__main__':
    sqlite_conn = sqlite3.connect('/home/jj/Code/ImportPGNs2017/players')
    sqlite_cur = sqlite_conn.cursor()

    conn = MongoClient("mongodb://ec2-54-158-98-180.compute-1.amazonaws.com:27017/pgns")
    cur = conn.pgns.allPGNs

    # trailing backslash important
    # pgn_folder = "./AllPGNs/"
    pgn_folder = "./Latest/"
    allInFolder(pgn_folder, cur, sqlite_conn, sqlite_cur)
