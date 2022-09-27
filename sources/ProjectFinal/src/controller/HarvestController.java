package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bean.Harvest;
import bean.Planting;
import bean.PlantingProgress;

@Controller
public class HarvestController {
	
	@RequestMapping(value = "/harvest/alllist", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listAllHarvest(HttpServletRequest request) {
		List<Harvest> harvest = null;
		try {
			HarvestManager hm = new HarvestManager();
			harvest = hm.listAllHarvest();
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
	
	@RequestMapping(value = "/harvest/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listHarvest(@RequestBody String plantid) {
		List<Harvest> harvest = null;
		try {
			HarvestManager hm = new HarvestManager();
			harvest = hm.listHarvest(plantid);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
	
	@RequestMapping(value = "/harvest/list_by_year", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listHarvestByYear(@RequestBody String year) {
		List<Harvest> harvest = null;
		try {
			HarvestManager hm = new HarvestManager();
			harvest = hm.listHarvestByYear(year);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
	
	@RequestMapping(value = "/harvest/listdetail", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listDetailHarvest(@RequestBody Map<String, String> map) {
		List<Harvest> harvest = null;
		try {
			String harvestid = map.get("HarvestID");
			String plantid = map.get("planting");
			HarvestManager hm = new HarvestManager();
			harvest = hm.listDetailHarvest(harvestid, plantid);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
	
	@RequestMapping(value = "/harvest/list_harvest_group_by_harvestid", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listHarvestGroupByHarvestID(@RequestBody String plantid) {
		List<Harvest> harvest = null;
		try {
			HarvestManager hm = new HarvestManager();
			harvest = hm.listHarvestGroupByHarvestID(plantid);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
	
	@RequestMapping(value = "/harvest/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addHarvest(@RequestBody Map<String, String> map) {
		String message = "";
		Harvest harvest = null;
		try {
			int HarvestID = Integer.parseInt(map.get("HarvestID"));
			Date harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("harvestDate"));
			String partName = map.get("partName");
			double qty = Double.parseDouble(map.get("qty"));
			String unit = map.get("unit");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			System.out.println(HarvestID + " " + harvestDate + " " + partName + " " + qty + " "
					+ unit + " " + note + " " + plantid);
			harvest = new Harvest(HarvestID, harvestDate, partName, qty, unit, note, planting);
			HarvestManager hm = new HarvestManager();
			message = hm.insert_harvest(harvest);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, harvest);
		}
	}
	
	@RequestMapping(value = "/harvest/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_updateHarvest(@RequestBody Map<String, String> map) {
		String message = "";
		Harvest harvest = null;
		try {
			int HarvestID = Integer.parseInt(map.get("HarvestID"));
			Date harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("harvestDate"));
			String partName = map.get("partName");
			double qty = Double.parseDouble(map.get("qty"));
			String unit = map.get("unit");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			System.out.println(HarvestID + " " + harvestDate + " " + partName + " " + qty + " "
					+ unit + " " + note + " " + plantid);
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			harvest = new Harvest(HarvestID, harvestDate, partName, qty, unit, note, planting);
			
			HarvestManager hm = new HarvestManager();
			message = hm.update_harvest(harvest);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, message);
		}
	}

	@RequestMapping(value = "/harvest/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_deleteHarvest(@RequestBody Map<String, String> map) {
		String message = "";
		Harvest harvest = null;
		try {
			int HarvestID = Integer.parseInt(map.get("HarvestID"));
			Date harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("harvestDate"));
			String note = map.get("note");
			String plantid = map.get("planting");
			
			System.out.println(HarvestID + " " + harvestDate + " " + note + " " + plantid);
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			harvest = new Harvest(HarvestID, harvestDate, "", 0, "", note, planting);
			
			HarvestManager hm = new HarvestManager();
			message = hm.delete_harvest(harvest);
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
	
	@RequestMapping(value = "/harvest/deletedetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_deleteDetailHarvest(@RequestBody Map<String, String> map) {
		String message = "";
		Harvest harvest = null;
		try {
			int HarvestID = Integer.parseInt(map.get("HarvestID"));
			Date harvestDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("harvestDate"));
			String partName = map.get("partName");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			System.out.println(HarvestID + " " + harvestDate + " " + note + " " + plantid);
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			harvest = new Harvest(HarvestID, harvestDate, partName, 0, "", note, planting);
			
			HarvestManager hm = new HarvestManager();
			message = hm.delete_Detailharvest(harvest);
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
	
	@RequestMapping(value = "/harvest/list_harvest_by_month", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listDetailHarvestByMonth(@RequestBody String month) {
		List<Harvest> harvest = null;
		try {
			HarvestManager hm = new HarvestManager();
			System.out.println(month);
			harvest = hm.listDetailHarvestByMonth(month);
			return new ResponseObj(200, harvest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, harvest);
	}
}
