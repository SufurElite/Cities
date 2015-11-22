/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.cities.deco;

import java.util.EnumSet;
import java.util.Set;

import org.terasology.cities.BlockTheme;
import org.terasology.cities.BlockType;
import org.terasology.cities.raster.RasterTarget;
import org.terasology.commonworld.heightmap.HeightMap;
import org.terasology.math.Side;
import org.terasology.math.geom.ImmutableVector3i;

/**
 * Converts {@link ColumnDecoration} into blocks
 */
public class ColumnRasterizer extends DecorationRasterizer<ColumnDecoration> {

    /**
     * @param theme the block theme to use
     */
    public ColumnRasterizer(BlockTheme theme) {
        super(theme, ColumnDecoration.class);
    }

    @Override
    public void raster(RasterTarget target, ColumnDecoration deco, HeightMap hm) {
        ImmutableVector3i pos = deco.getBasePos();
        int y = pos.getY();
        if (target.getAffectedArea().contains(pos.getX(), pos.getZ())) {
            if (y + deco.getBlockTypes().size() - 1 >= target.getMinHeight() && y <= target.getMaxHeight()) {
                for (int i = 0; i < deco.getHeight(); i++) {
                    BlockType type = deco.getBlockTypes().get(i);
                    Side side = deco.getSides().get(i);
                    Set<Side> sides = (side == null) ? EnumSet.noneOf(Side.class) : EnumSet.of(side);
                    target.setBlock(pos.getX(), y, pos.getZ(), type, sides);
                    y++;
                }
            }
        }
    }
}
