package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Harvest;
import bean.Order;
import bean.Planting;
import bean.User;

@Controller
public class PlantingController {

	@RequestMapping(value = "/planting/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listPlanting(HttpServletRequest request) {
		List<Planting> planting = null;
		try {
			PlantingManager pm = new PlantingManager();
			planting = pm.listAllPlanting();
			System.out.println(planting.toString());
			return new ResponseObj(200, planting);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, planting);
	}
	
	@RequestMapping(value = "/planting/list_by_year", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listPlantingByYear(@RequestBody String year) {
		List<Planting> planting = null;
		try {
			PlantingManager pm = new PlantingManager();
			planting = pm.listPlantingByYear(year);
			return new ResponseObj(200, planting);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, planting);
	}
	
	@RequestMapping(value = "/planting/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addPlanting(@RequestBody Map<String, String> map) {
		String message = "";
		Planting planting = null;
		try {
			int plantID = Integer.parseInt(map.get("plantID"));
			Date plantDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("plantDate"));
			Date exp_harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("exp_harvestDate"));
			double pay = Double.parseDouble(map.get("pay"));
			double discard = Double.parseDouble(map.get("discard"));
			double plant = Double.parseDouble(map.get("plant"));
			int sqaure_meters = Integer.parseInt(map.get("sqaure_meters"));
			int sqaure_wa = Integer.parseInt(map.get("sqaure_wa"));
			int ngar = Integer.parseInt(map.get("ngar"));
			int rai = Integer.parseInt(map.get("rai"));
			String cropid = map.get("cropid");
			String how_plant = map.get("how_plant");
			String note = map.get("note");
			String status = map.get("status");
			
			System.out.println(plantID + " " + plantDate + " " + exp_harvestDate + " " + pay + " " + discard + " "
					+ plant + " " + sqaure_meters + " " + sqaure_wa + " " + ngar + " " + rai + " " + cropid + " " + how_plant  + " " + note  + " " + status);
			planting = new Planting(plantID, plantDate, exp_harvestDate, pay, discard, plant, sqaure_meters, sqaure_wa, ngar, rai, cropid, how_plant, note, status);
			PlantingManager pm = new PlantingManager();
			message = pm.insert_planting(planting);
			return new ResponseObj(200, planting);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, planting);
		}
	}
	
	@RequestMapping(value = "/planting/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_updatePlanting(@RequestBody Map<String, String> map) {
		String message = "";
		Planting planting = null;
		try {
			int plantID = Integer.parseInt(map.get("plantID"));
			Date plantDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("plantDate"));
			Date exp_harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("exp_harvestDate"));
			double pay = Double.parseDouble(map.get("pay"));
			double discard = Double.parseDouble(map.get("discard"));
			double plant = Double.parseDouble(map.get("plant"));
			int sqaure_meters = Integer.parseInt(map.get("sqaure_meters"));
			int sqaure_wa = Integer.parseInt(map.get("sqaure_wa"));
			int ngar = Integer.parseInt(map.get("ngar"));
			int rai = Integer.parseInt(map.get("rai"));
			String cropid = map.get("cropid");
			String how_plant = map.get("how_plant");
			String note = map.get("note");
			String status = map.get("status");
			
			System.out.println(plantID + " " + plantDate + " " + exp_harvestDate + " " + pay + " " + discard + " "
					+ plant + " " + sqaure_meters + " " + sqaure_wa + " " + ngar + " " + rai + " " + cropid + " " + how_plant  + " " + note  + " " + status);
			planting = new Planting(plantID, plantDate, exp_harvestDate, pay, discard, plant, sqaure_meters, sqaure_wa, ngar, rai, cropid, how_plant, note, status);
			PlantingManager pm = new PlantingManager();
			message = pm.update_planting(planting);
			return new ResponseObj(200, planting);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, planting);
		}
	}

	@RequestMapping(value = "/planting/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_deletePlanting(@RequestBody Map<String, String> map) {
		String message = "";
		Planting planting = null;
		try {
			int plantID = Integer.parseInt(map.get("plantID"));
			Date plantDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("plantDate"));
			Date exp_harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("exp_harvestDate"));
			double pay = Double.parseDouble(map.get("pay"));
			double discard = Double.parseDouble(map.get("discard"));
			double plant = Double.parseDouble(map.get("plant"));
			int sqaure_meters = Integer.parseInt(map.get("sqaure_meters"));
			int sqaure_wa = Integer.parseInt(map.get("sqaure_wa"));
			int ngar = Integer.parseInt(map.get("ngar"));
			int rai = Integer.parseInt(map.get("rai"));
			String cropid = map.get("cropid");
			String how_plant = map.get("how_plant");
			String note = map.get("note");
			String status = map.get("status");
			
			PlantingManager pm = new PlantingManager();
			planting = new Planting(plantID, plantDate, exp_harvestDate, pay, discard, plant, sqaure_meters, sqaure_wa, ngar, rai, cropid, how_plant, note, status);
			message = pm.delete_planting(planting);
			if ("successfully delete".equals(message)) {
				return new ResponseObj(200, "1");
			}else {
				return new ResponseObj(200, "0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
	
	@RequestMapping(value = "/planting/get_planting", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_getPlanting(@RequestBody String plantid) {
		String message = "";
		Planting planting = null;
		try {
			System.out.println(plantid);
			PlantingManager pm = new PlantingManager();
			planting = pm.getPlanting(plantid);
			
			System.out.println(planting.toString());
			return new ResponseObj(200, planting);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, "0");
		}
	}
}
