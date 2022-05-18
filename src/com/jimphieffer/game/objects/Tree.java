package com.jimphieffer.game.objects;

import com.jimphieffer.game.objectTypes.*;
import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;

import java.util.UUID;

import static java.lang.Integer.parseInt;

public class Tree extends Static {

    @JsonDefaultConstructor(names = {"x", "y", "width", "height", "image", "id"})
    public Tree(double x, double y, int width, int height, String image, UUID id) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id);
    }

    public Tree() {
        super();
        setImage("textures/wood.png");
    }
}
