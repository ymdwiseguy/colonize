export function moveUnit(unitId, units, mapData, direction) {
    return units.map((unit) => {
        if (unit.unitId === unitId) {
            let from = {
                x: unit.xPosition,
                y: unit.yPosition
            };

            let to = getDestinationByDirection(from, direction);
            to = tryMoving(from, to, mapData, units, unit);

            return {
                ...unit,
                xPosition: to.x,
                yPosition: to.y
            }
        }
        return unit;
    });
}


export function getDestinationByDirection(from, direction) {
    let coords = {
        x: from.x,
        y: from.y
    };

    switch (direction) {
        case 'LEFT':
            if (coords.x > 1) {
                coords.x = coords.x - 1;
            }
            break;
        case 'RIGHT':
            coords.x = coords.x + 1;
            break;
        case 'UP':
            if (coords.y > 1) {
                coords.y = coords.y - 1;
            }
            break;
        case 'DOWN':
            coords.y = coords.y + 1;
            break;
    }
    return coords;
}

export function tryMoving(from, to, mapData, units, unit) {
    const mapWidth = mapData.width || 0;
    const mapHeight = mapData.height || 0;

    if (!moved(from, to)) {
        return from;
    }

    to = checkMapBoundaries(to, mapWidth, mapHeight);
    if (!moved(from, to)) {
        return from;
    }

    to = checkTerrainCollision(from, to, mapData, unit);
    if (!moved(from, to)) {
        return from;
    }
    to = checkUnitCollision(from, to, units, unit);

    return to;
}

export function moved(from, to) {
    return (from.x !== to.x || from.y !== to.y);
}

export function checkMapBoundaries(coords, mapWidth, mapHeight) {
    if (coords.x > mapWidth) {
        coords.x = mapWidth;
    }
    if (coords.y > mapHeight) {
        coords.y = mapHeight;
    }
    return coords;
}

export function checkTerrainCollision(from, to, mapData, unit) {
    let tileType = '';
    mapData.tiles.map((tile) => {
        if (tile.xCoordinate === to.x && tile.yCoordinate === to.y) {
            tileType = tile.type;
        }
    });
    if (!unitFitsTerrain(unit.unitType, tileType)) {
        to = from;
    }
    return to;
}

export function checkUnitCollision(from, to, units, unit) {
    let enemyUnitsAtDestination = 0;

    units.map((otherUnit) => {
        if (otherUnit.xPosition === to.x
            && otherUnit.yPosition === to.y
            && otherUnit.faction !== unit.faction) {
            enemyUnitsAtDestination++;
        }
    });

    if (enemyUnitsAtDestination > 0) {
        return from;
    } else {
        return to;
    }
}

export function unitFitsTerrain(unitType, tileType) {
    const waterUnit = (unitType === 'KARAVELLE');
    const waterTile = (tileType === 'OCEAN_SHALLOW' || tileType === 'OCEAN_DEEP');
    return waterUnit === waterTile;
}
