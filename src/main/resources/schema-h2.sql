-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE IF NOT EXISTS game_state (
    game_id VARCHAR,
    game_screen VARCHAR,
    world_map_id VARCHAR,
    cursor_x INT,
    cursor_y INT
);


CREATE TABLE IF NOT EXISTS worldmap (
    worldmap_id VARCHAR,
    title VARCHAR,
    worldmap_name VARCHAR,
    width INT,
    height INT
);

CREATE TABLE IF NOT EXISTS tile (
    tile_id VARCHAR,
    world_map_id VARCHAR,
    x_coordinate INT,
    y_coordinate INT,
    type VARCHAR
);

CREATE TABLE IF NOT EXISTS unit (
    unit_id VARCHAR,
    world_map_id VARCHAR,
    unit_type VARCHAR,
    active BOOLEAN,
    x_position INT,
    y_position INT
);
