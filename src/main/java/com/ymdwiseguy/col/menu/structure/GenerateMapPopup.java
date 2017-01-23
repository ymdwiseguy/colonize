package com.ymdwiseguy.col.menu.structure;

import java.util.List;

import static com.ymdwiseguy.col.menu.structure.PopupType.GENERATE_MAP;

public class GenerateMapPopup extends PopupMenu {

    private List<InputBlock> inputs;
    private MenuEntry submitButton;

    public GenerateMapPopup() {
        setType(GENERATE_MAP);
    }

    public List<InputBlock> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputBlock> inputs) {
        this.inputs = inputs;
    }

    public MenuEntry getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(MenuEntry submitButton) {
        this.submitButton = submitButton;
    }
}
