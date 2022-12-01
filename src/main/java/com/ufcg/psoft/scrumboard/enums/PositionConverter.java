package com.ufcg.psoft.scrumboard.enums;

import java.beans.PropertyEditorSupport;

public class PositionConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(Position.fromLabel(text));
    }
}
