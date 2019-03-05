package com.planed.ctlBot.utils;

public class StringConstants {
    public static final String CODE_ESCAPE = "```";
    public static final String MATCH_FORMATTER =
            "\n*******************************************************************************\n" +
                    "  The showdown is real! A Challenge has been accepted, it is g0-time boisss!   \n" +
                    "   {0} ()==[:::::::::> VS <::::::::::||===) {1}     \n" +
                    "*******************************************************************************";
    public static final String EXTENDED_FORMATTER =
                            "A challenge has been extended from {0}. Will {1} man up or will he wuzz out?";
    public static final String REJECTED_FORMATTER =
                                    "The honor of {1} has taken another hit. He rejected the challenge from {0}";
    public static final String RESULT_FORMATTER =
                                            "\n*******************************************************************************\n" +
                                                    "             @         That just in:  \n" +
                                                    "          @:::@                    \n" +
                                                    "       @.:/\\:/\\:.@         {1}   \n" +
                                                    "      ':\\@ @ @ @/:'                      \n" +
                                                    "        [@W@W@W@]                 got beaten     \n" +
                                                    "        `\"\"\"\"\"\"\"`           by the glorious    \n" +
                                                    "          {0}       \n" +
                                                    "*******************************************************************************";
    public static final String INFO_STRING = "```\n" +
            "-------------------------------------------------------------------------\n" +
            "*    __________________ .___.___                                        *\n" +
            "*   /   _____/\\_   ___ \\|   |   |      Starcraft 2 - DAS Liga           *\n" +
            "*   \\_____  \\ /    \\  \\/|   |   |                                       *\n" +
            "*   /        \\\\     \\___|   |   |   !hello, I'm a Discord-Bot.          *\n" +
            "*  /_______  / \\______  /___|___|   I run an automated starcraft        *\n" +
            "*          \\/         \\/            tournament. Just type !challenge    *\n" +
            "*   (D) iscord            to challenge anybody to play. The other       *\n" +
            "*   (A) utomated          person has to !accept the challenge. Then     *\n" +
            "*   (S) tarcraft          you battle it out in SCII and !report the     *\n" +
            "*                         result back to me. Every win or loss will     *\n" +
            "*      _     _            gain or loose you ELO points, impacting       *\n" +
            "*     | |   (_)                 your ranking. With !league you can      *\n" +
            "*     | |    _  __ _  __ _      take a look at the current rankings.    *\n" +
            "*     | |   | |/ _` |/ _` |                                             *\n" +
            "*     | |___| | (_| | (_| |         If you have any question, ask for   *\n" +
            "*     \\_____/_|\\__, |\\__,_|         !help                               *\n" +
            "*               __/ |                                                   *\n" +
            "*              |___/                 glhf                               *\n" +
            "*                                                                       *\n" +
            "*                                   DAS Liga Bot                        *\n" +
            "*                                                                       *\n" +
            "*    Join our server: https://discord.gg/pRvasZG                        *\n" +
            "* Readme: https://gitlab.com/Zoomstorm/DasLigaBot/blob/master/README.md *\n" +
            "*                                                                       *\n" +
            "-------------------------------------------------------------------------\n```";
    public static final String HELP_STRING = "```-------------------------Help Menu------------------------------------\n" +
            "*\n" +
            "* !league                 This displays the current league standings\n" +
            "* !info                   Displays some information about me!\n" +
            "* !list                   Lists all available commands\n" +
            "* !hello                  Hello World command\n" +
            "*\n" +
            "*           You can challenge people to play\n" +
            "* !challenge              Challenge a player. Just type @ and his name!\n" +
            "* !reject or !swipeleft   Reject your current challenge\n" +
            "* !revoke                 Revoke your current challenge\n" +
            "* !accept or !swiperight  Accept the challenge extended to you!\n" +
            "* !report                 Report either a 'win' or a 'loss' in the current game\n" +
            "* !race                   This command lets you change your race. Do you play Zerg, Terran, Protoss or Random?\n" +
            "* !status                 This displays your current status (open challenges, league position etc)\n" +
            "----------------------------------------------------------------------\n```";
}
