package com.gertu.mobile;

import java.io.Serializable;


public class Models implements Serializable{

    public class Deal implements Serializable {
        String _id;
        String name;
        String description;
        String price;
        String gertuprice;
        String shop;
        String image;
    }
}
