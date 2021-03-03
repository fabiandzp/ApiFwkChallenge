package themoviedb.entities;

public class List {

    private static int listId;
    private static Boolean listCreationValidation;

    public static int getListId() {
        return listId;
    }

    public static void setListId(int listId) {
        List.listId = listId;
    }

    public static Boolean getListCreationValidation() {
        return listCreationValidation;
    }

    public static void setListCreationValidation(Boolean listCreationValidation) {
        List.listCreationValidation = listCreationValidation;
    }


}
