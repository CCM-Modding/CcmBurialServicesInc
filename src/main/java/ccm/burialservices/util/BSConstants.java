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

package ccm.burialservices.util;

import com.google.common.base.Joiner;

public class BSConstants
{
    public static final String MODID   = "BurialServices";
    public static final String MODNAME = "CcmBurialServicesInc";

    public static final Joiner TEXT_JOINER = Joiner.on('\n');

    public static final String CHANNEL_SIGN_UPDATE   = MODID + "SU";
    public static final String CHANNEL_GRAVE_UPGRADE = MODID + "U";

    public static final String NBT_PLAYER_GRAVE_DATA   = "GraveData";
    public static final String NBT_PLAYER_DISABLEGRAVE = "DisableGrave";
    public static final String NBT_GRAVE_CAPACITY      = "capacity";
}
