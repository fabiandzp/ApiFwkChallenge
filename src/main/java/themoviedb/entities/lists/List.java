package themoviedb.entities.lists;

public class List {

    private static int listId;
    private static Boolean listCreationValidation;
    private static String createdBy;
    private static String description;

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

    public static String getCreatedBy() {
        return createdBy;
    }

    public static void setCreatedBy(String createdBy) {
        List.createdBy = createdBy;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        List.description = description;
    }
}
