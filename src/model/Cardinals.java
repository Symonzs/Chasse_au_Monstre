package model;

public enum Cardinals {
    NORTH, WEST, EAST, SOUTH;

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
