/*
Source Server         : BoulderDash
Source Server Version : 30706
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30706
File Encoding         : 65001

Date: 2012-12-18 08:42:57
*/

PRAGMA foreign_keys = OFF;

-- ----------------------------
-- Table structure for "main"."lastMap"
-- ----------------------------
DROP TABLE "main"."lastMap";
CREATE TABLE "lastMap" (
"Map"  TEXT(255) NOT NULL,
"Player"  TEXT(255) NOT NULL,
PRIMARY KEY ("Map", "Player")
);

-- ----------------------------
-- Table structure for "main"."lastPlayer"
-- ----------------------------
DROP TABLE "main"."lastPlayer";
CREATE TABLE "lastPlayer" (
"Nom"  TEXT(255) NOT NULL,
"Time"  INTEGER DEFAULT 0,
PRIMARY KEY ("Nom")
);

-- ----------------------------
-- Table structure for "main"."Score"
-- ----------------------------
DROP TABLE "main"."Score";
CREATE TABLE "Score" (
"Nom"  TEXT(255) NOT NULL ON CONFLICT ROLLBACK,
"Score"  INTEGER NOT NULL ON CONFLICT ROLLBACK DEFAULT 0,
"Time"  INTEGER NOT NULL ON CONFLICT ROLLBACK DEFAULT 0,
"MapHash"  TEXT(255) NOT NULL ON CONFLICT ROLLBACK,
PRIMARY KEY ("Nom" ASC, "Time" ASC, "MapHash") ON CONFLICT REPLACE
);

-- ----------------------------
-- Indexes structure for table lastMap
-- ----------------------------
CREATE UNIQUE INDEX "main"."index_Map"
ON "lastMap" ("Map" ASC);
CREATE UNIQUE INDEX "main"."index_Player"
ON "lastMap" ("Player" ASC);

-- ----------------------------
-- Indexes structure for table Score
-- ----------------------------
CREATE INDEX "main"."index_name"
ON "Score" ("Nom" ASC);
