
export function getMapOffset(state, cursorLeft, cursorTop) {

    const GRID_FACTOR = 100;

    const canvasWidth = state.viewPort.canvasWidth === 0 ? 0 : Math.floor(state.viewPort.canvasWidth / GRID_FACTOR);
    const canvasHeight = state.viewPort.canvasHeight === 0 ? 0 : Math.floor(state.viewPort.canvasHeight / GRID_FACTOR);
    const mapWidth = state.mapData.width;
    const mapHeight = state.mapData.height;
    let mapLeft = state.viewPort.mapOffsetX;
    let mapTop = state.viewPort.mapOffsetY;

    if (outsideViewPort(canvasWidth, canvasHeight, mapLeft, mapTop, cursorLeft, cursorTop)) {
        mapLeft = cursorLeft - canvasWidth / 2;
        mapLeft = limitHorizontally(mapLeft, mapWidth, canvasWidth);
        mapTop = cursorTop - canvasHeight / 2;
        mapTop = limitVertically(mapTop, mapHeight, canvasHeight);

        return {
            'xOffset': (mapLeft === 0 ? 0 : Math.floor(mapLeft)),
            'yOffset': (mapTop === 0 ? 0 : Math.floor(mapTop))
        };
    }
    return {'xOffset': state.viewPort.mapOffsetX, 'yOffset': state.viewPort.mapOffsetY};
}
export function getMapOffsetByCursor(state, cursor) {

    const GRID_FACTOR = 100;
    const cursorLeft = cursor.cursorX;
    const cursorTop = cursor.cursorY;

    const canvasWidth = state.viewPort.canvasWidth === 0 ? 0 : Math.floor(state.viewPort.canvasWidth / GRID_FACTOR);
    const canvasHeight = state.viewPort.canvasHeight === 0 ? 0 : Math.floor(state.viewPort.canvasHeight / GRID_FACTOR);
    const mapWidth = state.mapData.width;
    const mapHeight = state.mapData.height;
    let mapLeft = state.viewPort.mapOffsetX;
    let mapTop = state.viewPort.mapOffsetY;

    if (outsideViewPort(canvasWidth, canvasHeight, mapLeft, mapTop, cursorLeft, cursorTop)) {
        mapLeft = cursorLeft - canvasWidth / 2;
        mapLeft = limitHorizontally(mapLeft, mapWidth, canvasWidth);
        mapTop = cursorTop - canvasHeight / 2;
        mapTop = limitVertically(mapTop, mapHeight, canvasHeight);

        return {
            'xOffset': (mapLeft === 0 ? 0 : Math.floor(mapLeft)),
            'yOffset': (mapTop === 0 ? 0 : Math.floor(mapTop))
        };
    }
    return {'xOffset': state.viewPort.mapOffsetX, 'yOffset': state.viewPort.mapOffsetY};
}
export function getMapOffsetByUnits(state, units) {

    const GRID_FACTOR = 100;
    let cursorLeft = 0;
    let cursorTop = 0;

    units.map(unit => {
        if(unit.active){
            cursorLeft = unit.xPosition;
            cursorTop = unit.yPosition;
        }
    });

    const canvasWidth = state.viewPort.canvasWidth === 0 ? 0 : Math.floor(state.viewPort.canvasWidth / GRID_FACTOR);
    const canvasHeight = state.viewPort.canvasHeight === 0 ? 0 : Math.floor(state.viewPort.canvasHeight / GRID_FACTOR);
    const mapWidth = state.mapData.width;
    const mapHeight = state.mapData.height;
    let mapLeft = state.viewPort.mapOffsetX;
    let mapTop = state.viewPort.mapOffsetY;

    if (outsideViewPort(canvasWidth, canvasHeight, mapLeft, mapTop, cursorLeft, cursorTop)) {
        mapLeft = cursorLeft - canvasWidth / 2;
        mapLeft = limitHorizontally(mapLeft, mapWidth, canvasWidth);
        mapTop = cursorTop - canvasHeight / 2;
        mapTop = limitVertically(mapTop, mapHeight, canvasHeight);

        return {
            'xOffset': (mapLeft === 0 ? 0 : Math.floor(mapLeft)),
            'yOffset': (mapTop === 0 ? 0 : Math.floor(mapTop))
        };
    }
    return {'xOffset': state.viewPort.mapOffsetX, 'yOffset': state.viewPort.mapOffsetY};
}

export function outsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop) {
    const PADDING = 2;

    const rightOutside = (frameWidth + mapLeft - PADDING < cursorLeft);
    const leftOuside = (cursorLeft <= (PADDING + mapLeft));
    const topOuside = (cursorTop <= (PADDING + mapTop));
    const bottomOuside = (frameHeight + mapTop - PADDING < cursorTop);

    return (rightOutside || leftOuside || topOuside || bottomOuside);
}

export function reduceVisibleTiles(tiles, viewPort, action) {
    console.log(action.type);
    const visibility = visibleTiles(viewPort);
    return tiles.map((tile) => {
        if (tileVisible(tile, visibility)) {
            return {
                ...tile,
                visible: true
            }
        } else {
            return {
                ...tile,
                visible: false
            }
        }
    });
}

function tileVisible(tile, visibility) {
    return tile.xCoordinate > visibility.left
        && tile.xCoordinate <= visibility.right
        && tile.yCoordinate > visibility.top
        && tile.yCoordinate <= visibility.bottom;
}

function visibleTiles(viewPort) {
    const canvasWidth = Math.ceil(viewPort.canvasWidth / 100);
    const canvasHeight = Math.ceil(viewPort.canvasHeight / 100);
    const borderWidth = 3;

    return {
        left: viewPort.mapOffsetX - borderWidth,
        top: viewPort.mapOffsetY - borderWidth,
        right: viewPort.mapOffsetX + canvasWidth + borderWidth,
        bottom: viewPort.mapOffsetY + canvasHeight + borderWidth
    };
}

function limitHorizontally(mapLeft, mapWidth, frameWidth) {
    if (mapLeft < 0) {
        mapLeft = 0;
    }
    let limitRight = (mapWidth - mapLeft);
    if (limitRight < frameWidth) {
        mapLeft = mapWidth - frameWidth;
    }
    return mapLeft;
}

function limitVertically(mapTop, mapHeight, frameHeight) {
    if (mapTop < 0) {
        mapTop = 0;
    }
    let limitBottom = (mapHeight - mapTop);
    if (limitBottom < frameHeight) {
        mapTop = mapHeight - frameHeight;
    }
    return mapTop;
}
