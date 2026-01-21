package utils;

import exceptions.MissingDataException;
import javafx.scene.control.TextInputControl;

public class ValidationUtils {
    private static final String ERROR_STYLE_CLASS = "input-error";

    public static void validateNotEmpty(TextInputControl... fields) throws MissingDataException {
        boolean error = false;

        for (TextInputControl field : fields) {
            if(field.getText() == null || field.getText().trim().isEmpty()) {
                if(!field.getStyleClass().contains(ERROR_STYLE_CLASS)) {
                    field.getStyleClass().add(ERROR_STYLE_CLASS);
                }
                error = true;
            } else {
                field.getStyleClass().remove(ERROR_STYLE_CLASS);
            }
        }

        if(error) {
            throw new MissingDataException();
        }
    }

    public static void resetErrorOnType(TextInputControl... fields) {
        for(TextInputControl field : fields) {
            field.textProperty().addListener((_, _, _) ->
                    field.getStyleClass().remove(ERROR_STYLE_CLASS));
        }
    }
}
