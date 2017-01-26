package com.ymdwiseguy.col.menu.implementation;

import com.ymdwiseguy.col.menu.structure.InputBlock;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.menu.structure.PopupType.GENERATE_MAP;

public class GenerateMapPopup extends PopupMenu {

    private List<InputBlock> inputs;
    private MenuEntry submitButton;

    public GenerateMapPopup(String gameId) {
        setType(GENERATE_MAP);
        setHeader("Generate Map");

        List<InputBlock> inputs = new ArrayList<>();
        InputBlock inputBlockTitle = new InputBlock("title", "Title (e.g. Random map)");
        inputs.add(inputBlockTitle);
        InputBlock inputBlockName = new InputBlock("name", "Name (e.g. random_map_001)");
        inputs.add(inputBlockName);
        InputBlock inputBlockWidth = new InputBlock("width", "Width");
        inputs.add(inputBlockWidth);
        InputBlock inputBlockHeight = new InputBlock("height", "Height");
        inputs.add(inputBlockHeight);
        setInputs(inputs);

        MenuEntry submitButton = new MenuEntry("Generate", "/api/mapeditor/" + gameId + "/maps/generate");
        setSubmitButton(submitButton);
    }

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
