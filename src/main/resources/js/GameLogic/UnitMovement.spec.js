import test from 'ava';
import {
    checkMapBoundaries,
    checkUnitCollision,
    checkTerrainCollision,
    getDestinationByDirection,
    moved,
    moveUnit,
    tryMoving,
    unitFitsTerrain
} from './UnitMovement.jsx'
import * as tileTypes from '../Components/WorldMap/TileTypes.jsx';
import {factions} from '../Reducers/SimpleReducers.jsx'


// --- moved(from, to)
function movedMacro(t, to, expected) {
    t.is(moved({x: 10, y: 10}, to), expected);
}

test('A Unit did not move', movedMacro, {x: 10, y: 10}, false);
test('A Unit has moved', movedMacro, {x: 10, y: 11}, true);


// --- getDestinationByDirection(from, direction)
function getDestinationMacro(t, direction, expected) {
    t.deepEqual(getDestinationByDirection({x: 10, y: 10}, direction), expected);
}

test('getting a destination LEFT', getDestinationMacro, 'LEFT', {x: 9, y: 10});
test('getting a destination RIGHT', getDestinationMacro, 'RIGHT', {x: 11, y: 10});
test('getting a destination UP', getDestinationMacro, 'UP', {x: 10, y: 9});
test('getting a destination DOWN', getDestinationMacro, 'DOWN', {x: 10, y: 11});
test('getting a destination w/o direction', getDestinationMacro, 'foo', {x: 10, y: 10});


// --- checkMapBoundaries(coords, mapWidth, mapHeight)
test('keep it inside the box', t => {
    t.deepEqual(checkMapBoundaries({x: 10, y: 10}, 10, 10), {x: 10, y: 10})
});
test('move it into the box', t => {
    t.deepEqual(checkMapBoundaries({x: 10, y: 10}, 5, 5), {x: 5, y: 5})
});


// --- unitFitsTerrain(unitType, tileType)
function unitFitsTerrainMacro(t, unitType, terrainType, expected) {
    t.is(unitFitsTerrain(unitType, terrainType), expected)
}

unitFitsTerrainMacro.title = (providedTitle, unitType, terrainType, expected) => `${providedTitle} ${unitType}/${terrainType}`.trim();

test('sea unit fits sea', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.OCEAN_SHALLOW, true);
test('sea unit fits sea', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.OCEAN_DEEP, true);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_ARCTIC, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_DESERT, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_GRASS, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_HILLS, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_MARSH, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_MOUNTAINS, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_PLAINS, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_PRAIRIE, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_SAVANNAH, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_SWAMP, false);
test('sea unit does not fit land', unitFitsTerrainMacro, 'KARAVELLE', tileTypes.LAND_TUNDRA, false);
test('land unit does not fit sea', unitFitsTerrainMacro, 'SOLDIER', tileTypes.OCEAN_SHALLOW, false);
test('land unit does not fit sea', unitFitsTerrainMacro, 'SOLDIER', tileTypes.OCEAN_DEEP, false);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_ARCTIC, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_DESERT, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_GRASS, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_HILLS, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_MARSH, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_MOUNTAINS, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_PLAINS, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_PRAIRIE, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_SAVANNAH, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_SWAMP, true);
test('land unit fits land', unitFitsTerrainMacro, 'SOLDIER', tileTypes.LAND_TUNDRA, true);


// --- checkUnitCollision(from, to, units, unit)
function aUnit(id = 1, active = false, faction = factions[0], x = 10, y = 10) {
    return {
        unitType: "KARAVELLE",
        faction: faction,
        unitId: id,
        active: active,
        xPosition: x,
        yPosition: y,
        containingUnits: []
    }
}

function unitCollisionMacro(t, otherUnitsFaction, expected) {
    const units = [
        aUnit(1, true),
        aUnit(2, false, otherUnitsFaction, 10, 11)
    ];

    const unit = aUnit(1, true);

    t.deepEqual(
        checkUnitCollision({x: 10, y: 10}, {x: 10, y: 11}, units, unit),
        expected
    );
}

test(
    'unit collision: units of the same faction stack',
    unitCollisionMacro, factions[0], {x: 10, y: 11}
);

test(
    'unit collision: units of different factions dont stack',
    unitCollisionMacro, factions[1], {x: 10, y: 10}
);


// --- checkTerrainCollision(from, to, mapData, unit)
function aWaterTile(x = 1, y = 1) {
    return aTile(x, y, tileTypes.OCEAN_SHALLOW)
}

function aLandTile(x = 1, y = 1) {
    return aTile(x, y, tileTypes.LAND_PLAINS)
}

function aTile(x = 1, y = 1, type = tileTypes.OCEAN_SHALLOW) {
    return {
        xCoordinate: x,
        yCoordinate: y,
        type: type
    }
}

function terrainCollisionMacro(t, destinedTile, expected) {
    const mapData = {
        worldMapId: null,
        title: "Title",
        worldMapName: "some_map",
        tiles: [
            aWaterTile(1, 1),
            destinedTile
        ],
        width: 2,
        height: 1
    };
    t.deepEqual(
        checkTerrainCollision({x: 1, y: 1}, {x: 2, y: 1}, mapData, aUnit(1, true, 'faction', 1, 1)),
        expected
    )
}

test('unit can move on destined terrain', terrainCollisionMacro, aWaterTile(2, 1), {x: 2, y: 1});
test('unit can not move on destined terrain', terrainCollisionMacro, aLandTile(2, 1), {x: 1, y: 1});


// --- tryMoving(from, to, mapData, units, unit)
function setupForTryMovingTest(t, from, to, expected) {
    const mapData = {
        worldMapId: null,
        title: "Title",
        worldMapName: "some_map",
        tiles: [
            aWaterTile(1, 1),
            aWaterTile(1, 2),
            aLandTile(2, 1),
            aWaterTile(2, 2)
        ],
        width: 2,
        height: 2
    };
    const unit = aUnit(1, true, factions[0], 1, 1);
    const units = [
        unit,
        aUnit(2, false, factions[1], 2, 2)
    ];

    t.deepEqual(
        tryMoving(from, to, mapData, units, unit),
        expected
    );
}

test(
    'try moving within proper parameters is successful',
    setupForTryMovingTest, {x: 2, y: 1}, {x: 1, y: 1}, {x: 1, y: 1}
);

test(
    'try moving out of map boundaries fails',
    setupForTryMovingTest, {x: 2, y: 1}, {x: 3, y: 1}, {x: 2, y: 1}
);

test(
    'try moving on unpassable terrain fails',
    setupForTryMovingTest, {x: 2, y: 1}, {x: 2, y: 1}, {x: 2, y: 1}
);

test(
    'try moving over enemy unit fails',
    setupForTryMovingTest, {x: 2, y: 1}, {x: 2, y: 2}, {x: 2, y: 1}
);


// --- function moveUnit(unitId, units, mapData, direction)
test('moving a unit by id one step to the right', t => {

    let mapData = {
        worldMapId: null,
        title: "Title",
        worldMapName: "some_map",
        tiles: [
            aWaterTile(1, 1),
            aWaterTile(1, 2),
            aWaterTile(2, 1),
            aWaterTile(2, 2)
        ],
        width: 2,
        height: 2
    };
    let units = [
        aUnit(1, true, factions[0], 1, 1)
    ];


    let result = moveUnit(1, units, mapData, 'RIGHT');

    t.deepEqual(
        result,
        [aUnit(1, true, factions[0], 2, 1)]
    );

});



// --- function moveUnit(unitId, units, mapData, direction)
test('moving a land unit onto a water unit', t => {

    let mapData = {
        worldMapId: null,
        title: "Title",
        worldMapName: "some_map",
        tiles: [
            aLandTile(1, 1),
            aWaterTile(1, 2),
            aWaterTile(2, 1),
            aWaterTile(2, 2)
        ],
        width: 2,
        height: 2
    };

    let units = [
        {
            unitType: "KARAVELLE",
            faction: factions[0],
            unitId: 1,
            active: false,
            xPosition: 2,
            yPosition: 1,
            capacity: 2,
            containingUnits: []
        },{
            unitType: "SOLDIER",
            faction: factions[0],
            unitId: 2,
            active: true,
            xPosition: 1,
            yPosition: 1,
            capacity: 0,
            containingUnits: []
        }
    ];

    let result = moveUnit(2, units, mapData, 'RIGHT');

    t.is(result[1].xPosition, 2);

});
