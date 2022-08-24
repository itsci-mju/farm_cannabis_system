package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
public class PlantingProgressController {

	@RequestMapping(value = "/plantingprogress/alllist", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listAllPlantingProgress(HttpServletRequest request) {
		List<PlantingProgress> pp = null;
		try {
			PlantingProgressManager ppm = new PlantingProgressManager();
			pp = ppm.listAllPlantingProgress();
			return new ResponseObj(200, pp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, pp);
	}
	
	@RequestMapping(value = "/plantingprogress/list", method = RequestMethod.POST)
	public @ResponseBody ResponseObj do_listPlantingProgress(@RequestBody String plantid) {
		List<PlantingProgress> pp = null;
		try {
			PlantingProgressManager ppm = new PlantingProgressManager();
			pp = ppm.listPlantingProgress(plantid);
			return new ResponseObj(200, pp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseObj(500, pp);
	}
	
	@RequestMapping(value = "/plantingprogress/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_addplantingProgress(@RequestBody Map<String, String> map) {
		String message = "";
		PlantingProgress pp = null;
		try {
			int progressID = Integer.parseInt(map.get("progressID"));
			Date progressDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("progressDate"));
			String imgProgress = map.get("imgProgress");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			System.out.println(progressID + " " + progressDate + " " + imgProgress + " " + note + " " + plantid);
			pp = new PlantingProgress(progressID, progressDate, imgProgress, note, planting);
			PlantingProgressManager ppm = new PlantingProgressManager();
			message = ppm.insert_plantingProgress(pp);
			if(!imgProgress.equals("-")) {
				uploadFile(imgProgress);
			}
			
			return new ResponseObj(200, pp);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, pp);
		}
	}
	
	public Boolean uploadFile(String base64) {
		 String root = "/uploads";
		 final String[] base64Array = base64.split(",");
	     String dataUir, data;
	     //Build according to the specific file you represent
        dataUir = "data:image/jpg;base64";
        data = base64Array[0];
        byte[] decoded = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        try (FileOutputStream stream = new FileOutputStream(root)) {
       	    stream.write(decoded);
       	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     return true;
	 }
	
	@RequestMapping(value = "/plantingprogress/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_updatePlantingProgress(@RequestBody Map<String, String> map) {
		String message = "";
		PlantingProgress plantingprogress = null;
		try {
			int progressID = Integer.parseInt(map.get("progressID"));
			Date progressDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("progressDate"));
			String imgProgress = map.get("imgProgress");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			System.out.println(progressID + " " + progressDate + " " + imgProgress + " " + note + " "
					+ plantid);
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			plantingprogress = new PlantingProgress(progressID, progressDate, imgProgress, note, planting);
			
			PlantingProgressManager ppm = new PlantingProgressManager();
			message = ppm.update_plantingProgress(plantingprogress);
			return new ResponseObj(200, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Please try again....";
			return new ResponseObj(500, message);
		}
	}

	@RequestMapping(value = "/plantingprogress/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseObj do_deletePlantingProgress(@RequestBody Map<String, String> map) {
		String message = "";
		PlantingProgress plantingprogress = null;
		try {
			int progressID = Integer.parseInt(map.get("progressID"));
			Date progressDate = new SimpleDateFormat("dd-MM-yyyy").parse(map.get("progressDate"));
			String imgProgress = map.get("imgProgress");
			String note = map.get("note");
			String plantid = map.get("planting");
			
			System.out.println(progressID + " " + progressDate + " " + imgProgress + " " + note + " "
					+ plantid);
			
			PlantingManager pm = new PlantingManager();
			Planting planting = pm.getPlanting(plantid);
			
			plantingprogress = new PlantingProgress(progressID, progressDate, imgProgress, note, planting);
			
			PlantingProgressManager ppm = new PlantingProgressManager();
			message = ppm.delete_plantingProgress(plantingprogress);
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
}
