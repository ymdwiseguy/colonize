
export function getMapOffset(state, cursorLeft, cursorTop) {

    const GRID_FACTOR = 100;

    const canvasWidth = state.viewPort.canvasWidth === 0 ? 0 : Math.floor(state.viewPort.canvasWidth / GRID_FACTOR);
    const canvasHeight = state.viewPort.canvasHeight === 0 ? 0 : Math.floor(state.viewPort.canvasHeight / GRID_FACTOR);
    const mapWidth = state.mapData.width;
    const mapHeight = state.mapData.height;
    let mapLeft = state.mapOffsetX;
    let mapTop = state.mapOffsetY;

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
    return {'xOffset': state.mapOffsetX, 'yOffset': state.mapOffsetY};
}

export function outsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop) {
    const PADDING = 2;

    const rightOutside = (frameWidth + mapLeft - PADDING < cursorLeft);
    const leftOuside = (cursorLeft <= (PADDING + mapLeft));
    const topOuside = (cursorTop <= (PADDING + mapTop));
    const bottomOuside = (frameHeight + mapTop - PADDING < cursorTop);

    return (rightOutside || leftOuside || topOuside || bottomOuside);
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
