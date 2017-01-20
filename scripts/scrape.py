# begin:    http://www.chessgames.com/perl/chessgame?gid=1820394
# end:      http://www.chessgames.com/perl/chessgame?gid=1820804
import time
import re
import urllib2
from BeautifulSoup import BeautifulSoup

if __name__ == '__main__':
    file = open("WorldChampionship2016.pgn", "a")

    # for id in range(1820646, 1820804):
    # for id in [1848607, 1848606, 1848605, 1848598, 1848592, 1848578, 1848571, 1848564, 1848549, 1848222, 1848218, 1848217, 1848216, 1847850, 1847725, 1847603]:
    for id in [1848578]:
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