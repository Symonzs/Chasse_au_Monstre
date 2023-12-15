package model;

public enum Cardinals {
    NORTH, SOUTH, EAST, WEST;

    public static Cardinals getOpposite(Cardinals cardinals) {
        switch (cardinals) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            default:
                return null;
        }
    }
}
