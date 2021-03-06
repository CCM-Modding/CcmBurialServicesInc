/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Dries K. Aka Dries007 and the CCM modding crew.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ccm.burialservices;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class BSConfig
{
    public int villagerID   = 456418; //Rnd number, must be unique.
    public int toolBlockID  = 270;
    public int graveBlockID = 271;
    public final String[][] RIPText;
    public int defaultGraveSize = 1;
    public boolean giveSkull = true;
    public int exclusiveTimer = 60 * 5 * 20;
    public int decomposeTimer = 60 * 5 * 20;

    public BSConfig(File suggestedConfigurationFile)
    {
        final Configuration config = new Configuration(suggestedConfigurationFile);

        toolBlockID = config.getBlock("toolBlockID", toolBlockID).getInt();
        graveBlockID = config.getBlock("graveBlockID", graveBlockID).getInt();

        villagerID = config.get(Configuration.CATEGORY_BLOCK, "villagerID", villagerID, "Not a real blockID but must be equal on server and client!").getInt();

        defaultGraveSize = config.get(Configuration.CATEGORY_GENERAL, "defaultGraveSize", defaultGraveSize, "Default size for graves, includes the held tool.").getInt();
        giveSkull = config.get(Configuration.CATEGORY_GENERAL, "giveSkull", giveSkull, "Drop a skull when the grave breaks").getBoolean(giveSkull);

        exclusiveTimer = config.get(Configuration.CATEGORY_GENERAL, "exclusiveTimer", exclusiveTimer, "Time only the owner can access the grave. In ticks.").getInt();
        decomposeTimer = config.get(Configuration.CATEGORY_GENERAL, "decomposeTimer", decomposeTimer, "Time it takes the grave to decompose, starts after exclusiveTimer. In ticks.").getInt();

        String[] RIPTextLine1 = {"RIP", "Here lies"};
        String[] RIPTextLine2 = {"Dries007", "ClayCorp", "Morton", "CaptainShadows", "Squidward", "Testificate", "John Doe", "Jane Doe", "Firefly", "Scrubs", "Neo", "Ned Stark", "Villager #1", "Villager #2", "Villager #3", "Villager #4", "Villager #5", "Villager #6", "Villager #7", "Villager #8", "Villager #9"};
        String[] RIPTextLine3 = {" ", " ", "Respawn in 3 2 1", "'Stupid lag'", "'I died.'", "'Its a trap!'", "'Squids!'", "u done goofed", "Your face goddammit!", "yippie kay yay mofo"};
        String[] RIPTextLine4 = {"&k1996&r-&k2014&r"};

        for (int i = 0; i < RIPTextLine1.length; i++) RIPTextLine1[i] = addQuotes(RIPTextLine1[i]);
        for (int i = 0; i < RIPTextLine2.length; i++) RIPTextLine2[i] = addQuotes(RIPTextLine2[i]);
        for (int i = 0; i < RIPTextLine3.length; i++) RIPTextLine3[i] = addQuotes(RIPTextLine3[i]);
        for (int i = 0; i < RIPTextLine4.length; i++) RIPTextLine4[i] = addQuotes(RIPTextLine4[i]);

        RIPTextLine1 = config.get(Configuration.CATEGORY_GENERAL, "RIPTextLine1", RIPTextLine1, "First line of text on signs in village graveyards. Use & for text formatting.").getStringList();
        RIPTextLine2 = config.get(Configuration.CATEGORY_GENERAL, "RIPTextLine2", RIPTextLine2, "Second line of text on signs in village graveyards. Use & for text formatting.").getStringList();
        RIPTextLine3 = config.get(Configuration.CATEGORY_GENERAL, "RIPTextLine3", RIPTextLine3, "Third line of text on signs in village graveyards. Use & for text formatting.").getStringList();
        RIPTextLine4 = config.get(Configuration.CATEGORY_GENERAL, "RIPTextLine4", RIPTextLine4, "Forth line of text on signs in village graveyards. Use & for text formatting.").getStringList();

        config.save();

        /**
         * Must be done AFTER save!
         */
        for (int i = 0; i < RIPTextLine1.length; i++) RIPTextLine1[i] = removeQuotes(RIPTextLine1[i]);
        for (int i = 0; i < RIPTextLine2.length; i++) RIPTextLine2[i] = removeQuotes(RIPTextLine2[i]);
        for (int i = 0; i < RIPTextLine3.length; i++) RIPTextLine3[i] = removeQuotes(RIPTextLine3[i]);
        for (int i = 0; i < RIPTextLine4.length; i++) RIPTextLine4[i] = removeQuotes(RIPTextLine4[i]);

        for (int i = 0; i < RIPTextLine1.length; i++) RIPTextLine1[i] = formatColors(RIPTextLine1[i]);
        for (int i = 0; i < RIPTextLine2.length; i++) RIPTextLine2[i] = formatColors(RIPTextLine2[i]);
        for (int i = 0; i < RIPTextLine3.length; i++) RIPTextLine3[i] = formatColors(RIPTextLine3[i]);
        for (int i = 0; i < RIPTextLine4.length; i++) RIPTextLine4[i] = formatColors(RIPTextLine4[i]);

        RIPText = new String[][] {RIPTextLine1, RIPTextLine2, RIPTextLine3, RIPTextLine4};
    }

    private String removeQuotes(String s)
    {
        if (s.startsWith("\"") && s.endsWith("\"")) return s.substring(1, s.length() - 1);
        else return s;
    }

    private String addQuotes(String s)
    {
        if (s.startsWith("\"") && s.endsWith("\"")) return s;
        else return "\"" + s + "\"";
    }

    public static String formatColors(String message)
    {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++)
        {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
            {
                b[i] = '\u00a7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}
