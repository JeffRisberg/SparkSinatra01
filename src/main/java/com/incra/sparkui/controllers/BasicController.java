package com.incra.sparkui.controllers;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.inject.Inject;
import com.incra.sparkui.utils.Standard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jeff Risberg
 * @since 02/11/16
 */
public class BasicController {
    private final static Logger logger = LoggerFactory.getLogger(BasicController.class);

    @Inject
    public BasicController() {
    }

    /**
     * The main dashboard screen
     */
    public ModelAndView index(Request req, Response res) {
        Map<String, Object> model = Maps.newHashMap();

        try {
            model.put("firstName", "Bob");
            model.put("lastName", "Jones");

            List<Map> animals = Lists.newArrayList();
            Map animal;

            animal = new HashMap<String,String>();
            animal.put("name", "Tiger");
            animal.put("color", "striped");
            animals.add(animal);

            animal = new HashMap<String,String>();
            animal.put("name", "Wolf");
            animal.put("color", "gray");
            animals.add(animal);

            animal = new HashMap<String,String>();
            animal.put("name", "Dog");
            animal.put("color", "black");
            animals.add(animal);

            animal = new HashMap<String,String>();
            animal.put("name", "Cat");
            animal.put("color", "tabby");
            animals.add(animal);

            animal = new HashMap<String,String>();
            animal.put("name", "Deer");
            animal.put("color", "red");
            animals.add(animal);

            model.put("animals", animals);

            return new ModelAndView(model, "basic.hbs");
        } catch(Exception e) {
            model.put("message", e.getMessage());
            model.put("stack_trace", Standard.stackTraceAsString(e));
            return new ModelAndView(model, "error.hbs");
        }
    }
}
