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

package org.terasology.cities.settlements;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.terasology.math.Region3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.BaseFacet2D;

/**
 *
 */
public class SettlementFacet extends BaseFacet2D {

    private Set<Settlement> settlements = new HashSet<>();

    public SettlementFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    public Set<Settlement> getSettlements() {
        return Collections.unmodifiableSet(settlements);
    }

}
