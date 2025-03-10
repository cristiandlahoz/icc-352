package org.example.controller;

import io.javalin.http.Context;
import org.example.model.Location;
import org.example.service.LocationService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller(path = "/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Get(path = "/")
    public void getLocations(Context ctx) {
        List<Location> locations = locationService.findAll();
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Lista de Ubicaciones");
        model.put("lista", locations);
        ctx.render("listar.html", model);
    }

    @Get(path = "/create")
    public void createLocationForm(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Crear Ubicación");
        model.put("accion", "/create");
        ctx.render("crearEditarVisualizar.html", model);
    }

    @Post(path = "/create")
    public void createLocation(Context ctx) {
        Double latitude = Double.parseDouble(ctx.formParam("latitude"));
        Double longitude = Double.parseDouble(ctx.formParam("longitude"));

        try {
            locationService.createLocation(latitude, longitude);
        } catch (Exception e) {
            System.out.println("Error creando ubicación: " + e.getMessage());
            ctx.status(400).result("Error al crear la ubicación.");
            return;
        }

        ctx.redirect("/locations");
    }

    @Get(path = "/update/{id}")
    public void editLocationForm(Context ctx) {
        Long locationId = Long.parseLong(ctx.pathParam("id"));

        locationService.findById(locationId).ifPresentOrElse(
                location -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("titulo", "Editar Ubicación " + location.getLocationId());
                    model.put("location", location);
                    model.put("accion", "/update");
                    ctx.render("crearEditarVisualizar.html", model);
                },
                () -> ctx.status(404).result("Ubicación no encontrada")
        );
    }

    @Post(path = "/update")
    public void updateLocation(Context ctx) {
        Long locationId = Long.parseLong(ctx.formParam("id"));
        Double latitude = Double.parseDouble(ctx.formParam("latitude"));
        Double longitude = Double.parseDouble(ctx.formParam("longitude"));

        try {
            locationService.updateLocation(locationId, latitude, longitude);
        } catch (Exception e) {
            System.out.println("Error actualizando ubicación: " + e.getMessage());
            ctx.status(400).result("Error al actualizar la ubicación.");
            return;
        }

        ctx.redirect("/locations");
    }

    @Post(path = "/delete/{id}")
    public void deleteLocation(Context ctx) {
        Long locationId = Long.parseLong(ctx.pathParam("id"));

        try {
            locationService.deleteLocation(locationId);
        } catch (Exception e) {
            System.out.println("Error eliminando ubicación: " + e.getMessage());
            ctx.status(400).result("Error al eliminar la ubicación.");
            return;
        }

        ctx.redirect("/locations");
    }
}
