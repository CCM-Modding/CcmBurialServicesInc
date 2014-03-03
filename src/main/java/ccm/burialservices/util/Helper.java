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

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class Helper
{
    public static <T> T getRandomFromSet(Random random, T... collection)
    {
        return getRandomFromSet(random, Arrays.asList(collection));
    }

    public static <T> T getRandomFromSet(Random random, Collection<T> collection)
    {
        if (collection.isEmpty()) return null;
        if (collection.size() == 1) //noinspection unchecked
            return (T) collection.toArray()[0];
        int item = random.nextInt(collection.size());
        int i = 0;
        for (T obj : collection)
        {
            if (i == item) return obj;
            i = i + 1;
        }
        return null;
    }
}