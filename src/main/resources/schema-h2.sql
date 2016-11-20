CREATE TABLE IF NOT EXISTS worldmap (
    worldmap_id VARCHAR,
    title VARCHAR,
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
