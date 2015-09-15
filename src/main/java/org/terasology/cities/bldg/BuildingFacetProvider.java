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

package org.terasology.cities.bldg;

import java.awt.Rectangle;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.cities.common.Edges;
import org.terasology.cities.model.roof.HipRoof;
import org.terasology.cities.model.roof.Roof;
import org.terasology.cities.parcels.Parcel;
import org.terasology.cities.parcels.ParcelFacet;
import org.terasology.cities.surface.InfiniteSurfaceHeightFacet;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2f;
import org.terasology.math.geom.LineSegment;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Produces a {@link BuildingFacet}.
 */
@Produces(BuildingFacet.class)
@Requires({@Facet(ParcelFacet.class), @Facet(SurfaceHeightFacet.class)})
public class BuildingFacetProvider implements FacetProvider {

    private static final Logger logger = LoggerFactory.getLogger(BuildingFacetProvider.class);

    private final Cache<Parcel, Set<Building>> cache = CacheBuilder.newBuilder().build();

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(BuildingFacet.class);
        BuildingFacet facet = new BuildingFacet(region.getRegion(), border);

        ParcelFacet parcelFacet = region.getRegionFacet(ParcelFacet.class);
        InfiniteSurfaceHeightFacet heightFacet = region.getRegionFacet(InfiniteSurfaceHeightFacet.class);

        for (Parcel parcel : parcelFacet.getParcels()) {
            Set<Building> bldgs;
            try {
                bldgs = cache.get(parcel, () -> generateBuildings(parcel, heightFacet));
                for (Building bldg : bldgs) {
                    facet.addBuilding(bldg);
                }
            } catch (ExecutionException e) {
                logger.error("Could not compute buildings for {}", region.getRegion(), e);
            }
        }

        region.setRegionFacet(BuildingFacet.class, facet);
    }

    private Set<Building> generateBuildings(Parcel parcel, InfiniteSurfaceHeightFacet heightFacet) {

        DefaultBuilding b = new DefaultBuilding(parcel.getOrientation());
        Rect2i layout = new Rect2i(parcel.getShape());
        layout.expand(new Vector2i(-2, -2));

        Rect2i fenceRc = new Rect2i(parcel.getShape());
        LineSegment seg = Edges.getEdge(fenceRc, parcel.getOrientation());
        Vector2i gatePos = new Vector2i(BaseVector2f.lerp(seg.getStart(), seg.getEnd(), 0.5f), RoundingMode.HALF_UP);

        int floorHeight = TeraMath.floorToInt(heightFacet.getWorld(gatePos.x(), gatePos.y()));
        int wallHeight = 3;

        int roofPitch = 1;
        int roofBaseHeight = floorHeight + wallHeight;
        Rect2i roofArea = new Rect2i(layout);
        roofArea.expand(new Vector2i(1, 1));
        Rectangle awtRc = new Rectangle(roofArea.minX(), roofArea.minY(), roofArea.width(), roofArea.height());
        Roof roof = new HipRoof(awtRc, roofBaseHeight, roofPitch, roofBaseHeight + 1);

        RectBuildingPart part = new RectBuildingPart(layout, roof, floorHeight, wallHeight);
        b.addPart(part);

        return Collections.singleton(b);
    }

}
