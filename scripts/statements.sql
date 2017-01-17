CREATE TABLE "GamesBlack" (_id Integer PRIMARY KEY, Event Text, Site Text, Date Text, Round Text, WhiteID Integer, BlackID Integer, Result Text, WhiteELO Text, BlackELO Text, ECO Text, PGN Text)

CREATE TABLE "GamesUnsorted" (_id Integer PRIMARY KEY, Event Text, Site Text, Date Text, Round Text, WhiteID Integer, BlackID Integer, Result Text, WhiteELO Text, BlackELO Text, ECO Text, PGN Text)

CREATE TABLE "GamesWhite" (_id Integer PRIMARY KEY, Event Text, Site Text, Date Text, Round Text, WhiteID Integer, BlackID Integer, Result Text, WhiteELO Text, BlackELO Text, ECO Text, PGN Text)

CREATE TABLE `Players` (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`Name`	TEXT,
	OfficialID INTEGER NULL
)

CREATE TABLE `PlayersUnsorted` (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`Name`	TEXT
)

CREATE INDEX index_BlackID ON GamesBlack(BlackID)

CREATE INDEX index_WhiteID ON GamesWhite(WhiteID)


SELECT *
FROM PlayersUnsorted
WHERE Name GLOB ('*[^'||char(1,45,127)||']*');

INSERT INTO Players (Name)
SELECT PlayersUnsorted.Name
FROM PlayersUnsorted
LEFT JOIN Players ON PlayersUnsorted.Name = Players.Name
WHERE IfNull(Players.Name, '') = ''  AND PlayersUnsorted.Name <> ''

INSERT INTO GamesBlack
(Event, Site, Date, Round, WhiteID, BlackID, Result, WhiteELO, BlackELO, ECO, PGN)
SELECT GU.Event, GU.Site, GU.Date, GU.Round, GU.WhiteID, GU.BlackID, GU.Result, GU.WhiteELO, GU.BlackELO, GU.ECO, GU.PGN
FROM GamesUnsorted GU
LEFT JOIN GamesBlack GB ON
GU.WhiteID  = GB.WhiteID AND
GU.BlackID  = GB.BlackID AND
GU.PGN = GB.PGN
WHERE IfNull(GB.BlackID, 0) = 0

SELECT GU.Event, GU.Site, GU.Date, GU.Round, GU.WhiteID, GU.BlackID, GU.Result, GU.WhiteELO, GU.BlackELO, GU.ECO, GU.PGN
FROM GamesUnsorted GU
LEFT JOIN GamesWhite GW ON
GU.WhiteID  = GW.WhiteID AND
GU.BlackID  = GW.BlackID AND
GU.PGN = GW.PGN
WHERE IfNull(GW.WhiteID, 0) = 0

-- identify duplicates
SELECT count(*), pgn
FROM gamesWhite
GROUP BY pgn
HAVING count(*) > 1

-- back up one copy of dupes
INSERT INTO GamesUnsorted
SELECT DISTINCT *
FROM GamesWhite
WHERE _id IN (
	SELECT _id
	FROM gamesWhite
	GROUP BY pgn
	HAVING count(*) > 1
);

-- delete duplicates
 DELETE FROM GamesWhite WHERE PGN IN (
	SELECT pgn
	FROM gamesWhite
	GROUP BY pgn
	HAVING count(*) > 1
)
 
