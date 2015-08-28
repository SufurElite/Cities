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

package org.terasology.cities.parcels;

import java.util.Collections;
import java.util.Set;

import org.terasology.cities.model.bldg.Building;
import org.terasology.math.geom.Rect2i;

import com.google.common.collect.Sets;

/**
 * A parcel where buildings can be placed on.
 */
public class Parcel {
    private final Rect2i shape;

    private final Set<Building> buildings = Sets.newHashSet();

    /**
     * @param shape the shape of the lot
     */
    protected Parcel(Rect2i shape) {
        this.shape = shape;
    }

    /**
     * @return the layout shape
     */
    public Rect2i getShape() {
        return this.shape;
    }

    /**
     * @param bldg the building to add
     */
    public void addBuilding(Building bldg) {
        buildings.add(bldg);
    }

    /**
     * @return an unmodifiable view on all buildings in this lot
     */
    public Set<Building> getBuildings() {
        return Collections.unmodifiableSet(buildings);
    }

}
