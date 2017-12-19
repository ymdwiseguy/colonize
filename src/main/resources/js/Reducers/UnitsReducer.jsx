import {
    CURSOR_GOTO,
    UNIT_CLICKED,
} from '../ActionTypes/ActionTypes.jsx';

export const unitsInitialState = [
    {
        "unitType": "KARAVELLE",
        "faction" : "Netherlands",
        "unitId": 1,
        "active": true,
        "xPosition": 48,
        "yPosition": 33,
        "containingUnits": []
    },
    {
        "unitType": "KARAVELLE",
        "faction" : "Netherlands",
        "unitId": 2,
        "active": false,
        "xPosition": 49,
        "yPosition": 33,
        "containingUnits": []
    },
    {
        "unitType": "KARAVELLE",
        "faction" : "Netherlands",
        "unitId": 3,
        "active": false,
        "xPosition": 47,
        "yPosition": 34,
        "containingUnits": []
    },
    {
        "unitType": "SOLDIER",
        "faction" : "Netherlands",
        "unitId": 101,
        "active": false,
        "xPosition": 46,
        "yPosition": 34,
        "containingUnits": []
    }
];

export function units(state = unitsInitialState, action) {
    switch (action.type) {

        case CURSOR_GOTO:
            return deactivateAllUnits(state);

        case UNIT_CLICKED:
            return state.map((unit) => {
                if (unit.unitId === action.unitId) {
                    return {
                        ...unit,
                        active: true
                    }
                } else {
                    return {
                        ...unit,
                        active: false
                    }
                }
            });

        default:
            return state;
    }
}


function deactivateAllUnits(units) {
    return units.map((unit) => {
        if (unit.active) {
            return {
                ...unit,
                active: false
            }
        }
        return unit;
    })
}
