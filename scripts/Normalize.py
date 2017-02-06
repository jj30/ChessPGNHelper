import sqlite3
import string
import sys

conn = sqlite3.connect('/home/jj/Code/ImportPGNs2017/players')
cur = conn.cursor()

# Agnos D
# Agnos Demetrios
# Agnos Demetrios (ENG)
# Agnos Demetrios ENG
# Agnos, D.
# Agnos, Demetrios

# Aarland S
# Aarland Stein
# Aarland Stein A
# Aarland Stein Arild
# Aarland Stein Arild (NOR)
# Aarland, Stein
# Aarland, Stein Arild


def doAllForLetter(alpha):
    sql = "SELECT _id, Name FROM player WHERE UPPER(Name) LIKE '" + alpha + "%';"
    names = cur.execute(sql)

    try:
        for id, n in names.fetchall():
            bGoodForm = False

            long_name_list = n.split(", ")
            l_name = long_name_list[0].strip()
            bIsThreeParts = len(n.split(" ")) == 3

            try:
                f_name = long_name_list[1].strip()
                f_initial = f_name[:1].strip()
                bGoodForm = len(long_name_list) == 2 and len(f_name) > 2 and not bIsThreeParts
            except:
                bGoodForm = False

            if bGoodForm:
                sql = "UPDATE player SET official_id = {0} \
                        WHERE (name in ('{1} {3}', '{1} {2}', '{1} {3}.', '{1}, {3}.', '{1}, {2}', '{1},{2}', '{1},{3}.', '{1},{3}') \
                        OR name like '{1} {2} %' OR name like '{1}, {2} %' OR _id = {0}) \
                        AND IfNull(official_id, 0) <> {0};"

                l_name = l_name.replace("'", "''")
                f_name = f_name.replace("'", "''")

                sql = sql.format(id, l_name, f_name, f_initial)
                num_recs = cur.execute(sql)
                if (num_recs.rowcount > 0):
                    # need to commit now, or recs get swept up in future updates (other names w goodform)
                    conn.commit()
                    print(sql)

    except:
        print "Unexpected error:", sys.exc_info()[0]
        pass

    conn.commit()

'''def dunn_goofed(cursor):
    # The OfficialID is not set in the original ID row
    sql = "SELECT OfficialID FROM Players WHERE OfficialID IS NOT NULL;"
    names = cursor.execute(sql)

    for (id,) in names.fetchall():
        sql = "UPDATE Players SET OfficialID = {0} WHERE _id = {0};".format(id)

        num_recs = cursor.execute(sql)
        if (num_recs.rowcount > 0):
            print(sql)'''

if __name__ == '__main__':
    for letter in string.lowercase[3:26]:
        doAllForLetter(letter.upper())


    # python performs better without having to ref global vars
    # dunn_goofed(cur)






