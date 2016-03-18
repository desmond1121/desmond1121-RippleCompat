/*
 * Copyright (C) 2016 Jonatan Salas
 * Copyright (C) 2015 Desmond Yao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.desmond.ripple.factory;

import android.graphics.Path;

import com.desmond.ripple.util.RippleUtil;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class RipplePathFactory {

    public static Path produceCirclePath(){
        Path path = new Path();
        path.addCircle(0, 0, RippleUtil.MIN_RIPPLE_RADIUS, Path.Direction.CW);
        return path;
    }

    public static Path produceHeartPath(){
        int d = RippleUtil.MIN_RIPPLE_RADIUS * 2;
        int offset = (int)(-d /2f);
        Path path = new Path();
        path.moveTo(0, offset);
        path.cubicTo(-d, offset - d / 2, -d, offset + d / 2, 0, offset + d);
        path.cubicTo(d, offset + d / 2, d, offset - d / 2, 0, offset);
        path.close();
        return path;
    }

    public static Path produceTrianglePath(){
        int d = RippleUtil.MIN_RIPPLE_RADIUS * 2;
        int offset = (int)(-d * 2 / 3f);
        int edge = (int) ( d * 2 / Math.sqrt(3));
        Path path = new Path();
        path.moveTo(0, offset);
        path.lineTo(-edge / 2f, -offset / 2f);
        path.lineTo(edge / 2f, -offset / 2f);
        path.close();
        return path;
    }
}
