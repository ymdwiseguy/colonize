export function moveUnit(unitId, units, mapData, direction) {

    return units.map((unit) => {
        if (unit.unitId === unitId) {

            const from = [unit.xPosition, unit.yPosition];

            let to = getDestinationByDirection(from, direction);
            to = tryMoving(from, to, mapData, units, unit);

            return {
                ...unit,
                xPosition: to[0],
                yPosition: to[1]
            }

        }
        return unit;
    });
}


function getDestinationByDirection(from, direction) {
    let destinationX = from[0];
    let destinationY = from[1];

    switch (direction) {
        case 'LEFT':
            if (destinationX > 1) {
                destinationX = destinationX - 1;
            }
            break;
        case 'RIGHT':
            destinationX = destinationX + 1;
            break;
        case 'UP':
            if (destinationY > 1) {
                destinationY = destinationY - 1;
            }
            break;
        case 'DOWN':
            destinationY = destinationY + 1;
            break;
    }
    return [destinationX, destinationY];
}


function tryMoving(from, to, mapData, units, unit) {

    const mapWidth = mapData.width || 0;
    const mapHeight = mapData.height || 0;

    if (!moved(from, to)) {
        return from;
    }
    to = checkMapBoundaries(to, mapWidth, mapHeight);
    if (!moved(from, to)) {
        return from;
    }
    to = checkTerrainCollision(from, to, mapData);
    if (!moved(from, to)) {
        return from;
    }
    to = checkUnitCollision(from, to, units, unit);

    return to;
}


function moved(from, to) {
    if (from[0] !== to[0]) {
        return true;
    }
    return from[1] !== to[1];

}


function checkMapBoundaries(coords, mapWidth, mapHeight) {
    let x = coords[0];
    let y = coords[1];

    if (x > mapWidth) {
        x = mapWidth;
    }
    if (y > mapHeight) {
        y = mapHeight;
    }

    return [x, y];
}


function checkTerrainCollision(from, to, mapData) {
    let x = to[0];
    let y = to[1];

    let tileType = '';
    mapData.tiles.map((tile) => {
        if (tile.xCoordinate === x && tile.yCoordinate === y) {
            tileType = tile.type;
        }
    });
    if (tileType !== 'OCEAN_SHALLOW' && tileType !== 'OCEAN_DEEP') {
        x = from[0];
        y = from[1];
    }
    return [x, y];
}


function checkUnitCollision(from, to, units, unit) {
    let x = to[0];
    let y = to[1];
    let enemyUnitsAtDestination = 0;

    units.map((otherUnit) => {
        if (otherUnit.xPosition === x && otherUnit.yPosition === y && otherUnit.faction !== unit.faction) {
            enemyUnitsAtDestination++;
        }
    });

    if (enemyUnitsAtDestination > 0) {
        return from;
    } else {
        return to;
    }
}
