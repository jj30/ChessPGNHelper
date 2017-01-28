# begin:    http://www.chessgames.com/perl/chessgame?gid=1820394
# end:      http://www.chessgames.com/perl/chessgame?gid=1820804
import time
import re
import urllib2
from BeautifulSoup import BeautifulSoup

if __name__ == '__main__':
    file = open("McDonnelvLaBourdonnais.pgn", "a")

    for id in [1257910,1001854,1032320,1069169,1385670,1007846,1102384,1008419,1132699,1100774,1053943,1067175,1260278,1067209,1340361,1340393,1084375,1067294,1268187,1242884,1272702,1282608,1132189,1026352,1019058,1427466,1044267,1007465,1070747,1094915,1102104,1228798,1144002,1067846,1106725,1102400,1007571,1012099,1261710,1139363,1261680,1008371,1075094,1032537,1032787,1397132,1036342,1003826,1012326,1044324,1067319,1124533,1262430,1152958,1095025,1070732,1009910,1258270,1269834,1042533,1109097,1109111,1322027,1027914,1060694,1289665,1034337,1044022,1018785,1042835,1008421,1067858,1067017,1387996,1019762,1008397,1008397,1041460,1091272,1060750,1090846,1070074,1289099,1140914,1186636,1018961,1031957,1019060,1233404,1018910,1477101,1139729,1023029,1111459,1119679,1060180,1103322,1008361,1011478]:
        url = "http://www.chessgames.com/perl/chessgame?gid={0}".format(id)

        try:
            response = urllib2.urlopen(url)
            html = response.read()
            soup = BeautifulSoup(html)
            names_of_players = soup.findAll('a', attrs={'href': re.compile('perl/chessplayer')})

            title = soup.title.text

            # I hate regular expressions very much
            numbers = [str(i) for i in range(0, 10)]
            year = ""
            for letter in title:
                if letter in numbers:
                    year = year + letter

            player_one = names_of_players[0].contents[0]
            player_two = names_of_players[1].contents[0]
            one_last = player_one.split(" ")[1]
            two_last = player_two.split(" ")[1]

            # http: // www.chessgames.com / pgn / carlsen_rushfeldt_2010.pgn?gid = 1820804
            url2 = "http://www.chessgames.com/pgn/{0}_{1}_{2}.pgn?gid={3}".format(one_last, two_last, year, id)
            pgn_file = urllib2.urlopen(url2).read()
            pgn_file += "\n"
            file.write(pgn_file)
            file.flush()
            time.sleep(10)
        except:
            print ("Attempt to acess {0} resulted in failure.".format(url))