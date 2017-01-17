import sqlite3
import string

conn = sqlite3.connect('/home/jj/Code/ImportPGNs2017/PGNSDB2017')
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
    sql = "SELECT _id, Name FROM Players WHERE UPPER(Name) LIKE '{0}%';".format(alpha)
    names = cur.execute(sql)

    for id, n in names.fetchall():
        bGoodForm = False

        long_name_list = n.split(", ")
        l_name = long_name_list[0].strip()
        bIsThreeParts = n.split(" ").length == 3

        try:
            f_name = long_name_list[1].strip()
            f_initial = f_name[:1].strip()
            bGoodForm = len(long_name_list) == 2 and len(f_name) > 2 and not bIsThreeParts
        except:
            bGoodForm = False

        if bGoodForm:
            sql = "UPDATE Players SET OfficialID = {0} \
                    WHERE (Name in ('{1} {3}', '{1} {2}', '{1} {3}.', '{1}, {3}.', '{1}, {2}', '{1},{2}') \
                    OR Name like '{1} {2} %' OR Name like '{1}, {2} %' OR _id = {0}) \
                    AND OfficialID <> {0};"

            l_name = l_name.replace("'", "''")
            f_name = f_name.replace("'", "''")

            sql = sql.format(id, l_name, f_name, f_initial)
            num_recs = cur.execute(sql)
            if (num_recs.rowcount > 0):
                # need to commit now, or recs get swept up in future updates (other names w goodform)
                conn.commit()
                print(sql)

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
    for letter in string.lowercase[:26]:
        doAllForLetter(letter.upper())
    # python performs better without having to ref global vars
    # dunn_goofed(cur)






