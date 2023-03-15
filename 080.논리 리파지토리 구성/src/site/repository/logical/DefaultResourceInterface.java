package site.repository.logical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultResourceInterface implements IResourceInterface {
	/**
	 * 서비스명으로 소스검색하여 리소스ID 리스트를 리턴한다.(소스선택시)
	 * 
	 */
	public List getSrcIDListByServiceName(HashMap inHash) throws Exception {
	}
	
	/**
	 * 서비스명으로 [RES_PATH][RES_NAME][SERVICE_NAME]를 리턴한다.(서버신규자원 체크인시)
	 * 
	 */
	public List getResourcePathListByServiceName(HashMap inHash) throws Exception
	{
		
		/**
		 * @param : 서비스명 
		 * 
		 */
		String[] serviceNames = (String[]) inHash.get("SERVICE_NAMES");
		
		
		/**
		 * @desc : [RES_PATH][RES_NAME]
		 * 
		 */
		int size = serviceNames.length;
		ArrayList list = new ArrayList();
		//for(int i=0; i< size ; i++)
		//{
			list.addAll(getServiceModule());
			list.addAll(getBatchModule());
			list.addAll(getBizModule());
			list.addAll(getStructure());
			list.addAll(getDBIO());
		//}
		
		return list;
	}
	
	protected ArrayList getServiceModule() throws Exception
	{
		ArrayList list = new ArrayList();
		
		/*
		 * SERVICE_MODULE
		 */
	
	
		HashMap map = new HashMap();
		//물리파일(자식)
		map.put("RES_PATH", "/compile/AUT/src/serviceModule/SAUT10000A/");
		map.put("RES_NAME", "SAUT10000A.c");
		
		//논리모델(부모)
		map.put("PAR_RES_PATH", "/PROBUILDER/service_module/");
		map.put("PAR_RES_NAME", "SAUT10000A");
		
		list.add(map);
		//====================================================================
		
		HashMap map1 = new HashMap();
		//물리파일(자식)
		map1.put("RES_PATH", "/compile/AUT/src/serviceModule/SAUT10000A/");
		map1.put("RES_NAME", "SAUT10000A.h");
		
		//논리모델(부모)
		map1.put("PAR_RES_PATH", "/PROBUILDER/service_module/");
		map1.put("PAR_RES_NAME", "SAUT10000A");
		
		list.add(map1);
		//====================================================================
		
		HashMap map2 = new HashMap();
		//물리파일(자식)
		map2.put("RES_PATH", "/compile/AUT/src/serviceModule/SAUT10000A/xml/");
		map2.put("RES_NAME", "SAUT10000A.xml");
		
		//논리모델(부모)
		map2.put("PAR_RES_PATH", "/PROBUILDER/service_module/");
		map2.put("PAR_RES_NAME", "SAUT10000A");
		
		list.add(map2);
		//====================================================================
		
		return list;
	}
	
	protected ArrayList getBatchModule() throws Exception
	{
		ArrayList list = new ArrayList();
		
		/*
		 * BATCH_MODULE
		 */
		HashMap mapb = new HashMap();
		//물리파일(자식)
		mapb.put("RES_PATH", "/compile/COM/src/batch/");
		mapb.put("RES_NAME", "BCOM15002A.c");
		
		//논리모델(부모)
		mapb.put("PAR_RES_PATH", "/PROBUILDER/batch_module/");
		mapb.put("PAR_RES_NAME", "BCOM15002A");
		
		list.add(mapb);
		//====================================================================
		
		HashMap mapb1 = new HashMap();
		//물리파일(자식)
		mapb1.put("RES_PATH", "/compile/COM/src/batch/");
		mapb1.put("RES_NAME", "BCOM15002A.h");
		
		//논리모델(부모)
		mapb1.put("PAR_RES_PATH", "/PROBUILDER/batch_module/");
		mapb1.put("PAR_RES_NAME", "BCOM15002A");
		
		list.add(mapb1);
		//====================================================================
		
		HashMap mapb2 = new HashMap();
		//물리파일(자식)
		mapb2.put("RES_PATH", "/compile/COM/src/batch/xml/");
		mapb2.put("RES_NAME", "/BCOM15002A.xml");
		
		//논리모델(부모)
		mapb2.put("PAR_RES_PATH", "/PROBUILDER/batch_module/");
		mapb2.put("PAR_RES_NAME", "BCOM15002A");
		
		list.add(mapb2);
		//====================================================================
		
		return list;
	}
	
	protected ArrayList getBizModule() throws Exception
	{
		ArrayList list = new ArrayList();
		
		/*
		 * BIZ_MODULE
		 */
		HashMap mapbiz = new HashMap();
		//물리파일(자식)
		mapbiz.put("RES_PATH", "/compile/AUT/src/module/");
		mapbiz.put("RES_NAME", "maut_apvfru_pi.c");
		
		//논리모델(부모)
		mapbiz.put("PAR_RES_PATH", "/PROBUILDER/module/");
		mapbiz.put("PAR_RES_NAME", "maut_apvfru_pi");
		
		list.add(mapbiz);
		//====================================================================
		
		HashMap mapbiz1 = new HashMap();
		//물리파일(자식)
		mapbiz1.put("RES_PATH", "/compile/AUT/src/module/");
		mapbiz1.put("RES_NAME", "maut_apvfru_pi.h");
		
		//논리모델(부모)
		mapbiz1.put("PAR_RES_PATH", "/PROBUILDER/module/");
		mapbiz1.put("PAR_RES_NAME", "maut_apvfru_pi");
		
		list.add(mapbiz1);
		//====================================================================
		
		HashMap mapbiz2 = new HashMap();
		//물리파일(자식)
		mapbiz2.put("RES_PATH", "/compile/AUT/src/module/xml/");
		mapbiz2.put("RES_NAME", "maut_apvfru_pi.xml");
		
		//논리모델(부모)
		mapbiz2.put("PAR_RES_PATH", "/PROBUILDER/module/");
		mapbiz2.put("PAR_RES_NAME", "maut_apvfru_pi");
		
		list.add(mapbiz2);
		//====================================================================
		
		return list;
	}
	
	protected ArrayList getStructure() throws Exception
	{
		ArrayList list = new ArrayList();
		
		/*
		 * STRUCTURE
		 */
		HashMap mapstruc = new HashMap();
		//물리파일(자식)
		mapstruc.put("RES_PATH", "/release/pmap/src/");
		mapstruc.put("RES_NAME", "pfmMapperBAUT15002A01_dt.c");
		
		//논리모델(부모)
		mapstruc.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc);
		//====================================================================
		
		HashMap mapstruc1 = new HashMap();
		//물리파일(자식)
		mapstruc1.put("RES_PATH", "/release/pmap/inc/");
		mapstruc1.put("RES_NAME", "pfmMapperBAUT15002A01_dt.h");
		
		//논리모델(부모)
		mapstruc1.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc1.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc1);
		//====================================================================
		
		HashMap mapstruc2 = new HashMap();
		//물리파일(자식)
		mapstruc2.put("RES_PATH", "/release/pmap/xml/");
		mapstruc2.put("RES_NAME", "BAUT15002A01_dt.xml");
		
		//논리모델(부모)
		mapstruc2.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc2.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc2);
		//====================================================================
		
		HashMap mapstruc3 = new HashMap();
		//물리파일(자식)
		mapstruc3.put("RES_PATH", "/release/pmap/src/");
		mapstruc3.put("RES_NAME", "pfmMapperBAUT15002A01_dtMsgFld.c");
		
		//논리모델(부모)
		mapstruc3.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc3.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc3);
		//====================================================================
		
		HashMap mapstruc4 = new HashMap();
		//물리파일(자식)
		mapstruc4.put("RES_PATH", "/release/pmap/inc/");
		mapstruc4.put("RES_NAME", "pfmMapperBAUT15002A01_dtMsgFld.h");
		
		//논리모델(부모)
		mapstruc4.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc4.put("PAR_RES_NAME", "BAUT15002A01_dt");
                 list.add(mapstruc4);

		

		HashMap mapstruc5 = new HashMap();
		//물리파일(자식)
		mapstruc5.put("RES_PATH", "/release/pmap/xml/");
		mapstruc5.put("RES_NAME", "BAUT15002A01_dtMsgFld.xml");
		
		//논리모델(부모)
		mapstruc5.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc5.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc5);

		
		

		
		HashMap mapstruc6 = new HashMap();
		//물리파일(자식)
		mapstruc6.put("RES_PATH", "/release/pmap/src/");
		mapstruc6.put("RES_NAME", "pfmMapperBAUT15002A01_dtMsgDel.c");
		
		//논리모델(부모)
		mapstruc6.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc6.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc6);

		
		HashMap mapstruc7 = new HashMap();
		//물리파일(자식)
		mapstruc7.put("RES_PATH", "/release/pmap/inc/");
		mapstruc7.put("RES_NAME", "pfmMapperBAUT15002A01_dtMsgDel.h");
		
		//논리모델(부모)
		mapstruc7.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc7.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc7);

		

		HashMap mapstruc8 = new HashMap();
		//물리파일(자식)
		mapstruc8.put("RES_PATH", "/release/pmap/xml/");
		mapstruc8.put("RES_NAME", "BAUT15002A01_dtMsgDel.xml");
		
		//논리모델(부모)
		mapstruc8.put("PAR_RES_PATH", "/PROMAPPER/strcuture/");
		mapstruc8.put("PAR_RES_NAME", "BAUT15002A01_dt");
		
		list.add(mapstruc8);

		
		return list;
	}
	
	protected ArrayList getDBIO() throws Exception
	{
		ArrayList list = new ArrayList();
		
		/*
		 * DBIO
		 */
		HashMap map = new HashMap();
		//물리파일(자식)
		map.put("RES_PATH", "/release/dbio/src/");
		map.put("RES_NAME", "pfmDbioAAUTB1006_PF218.pc");
		
		//논리모델(부모)
		map.put("PAR_RES_PATH", "/DBIO/");
		map.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map);
		//====================================================================
		
		HashMap map1 = new HashMap();
		//물리파일(자식)
		map1.put("RES_PATH", "/release/dbio/src/");
		map1.put("RES_NAME", "pfmDbioAAUTB1006_PF218.c");
		
		//논리모델(부모)
		map1.put("PAR_RES_PATH", "/DBIO/");
		map1.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map1);
		//====================================================================
		
		HashMap map2 = new HashMap();
		//물리파일(자식)
		map2.put("RES_PATH", "/release/dbio/inc/");
		map2.put("RES_NAME", "pfmDbioAAUTB1006_PF218.h");
		
		//논리모델(부모)
		map2.put("PAR_RES_PATH", "/DBIO/");
		map2.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map2);
		//====================================================================
		
		HashMap map3 = new HashMap();
		//물리파일(자식)
		map3.put("RES_PATH", "/release/dbio/xml/");
		map3.put("RES_NAME", "AAUTB1006_PF218.xml");
		
		//논리모델(부모)
		map3.put("PAR_RES_PATH", "/DBIO/");
		map3.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map3);
		//====================================================================
		
		HashMap map4 = new HashMap();
		//물리파일(자식)
		map4.put("RES_PATH", "/release/pmap/src/");
		map4.put("RES_NAME", "pfmDbioAAUTB1006_PF218In.c");
		
		//논리모델(부모)
		map4.put("PAR_RES_PATH", "/DBIO/");
		map4.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map4);
		//====================================================================
		
		HashMap map5 = new HashMap();
		//물리파일(자식)
		map5.put("RES_PATH", "/release/pmap/inc/");
		map5.put("RES_NAME", "pfmDbioAAUTB1006_PF218In.h");
		
		//논리모델(부모)
		map5.put("PAR_RES_PATH", "/DBIO/");
		map5.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map5);
		//====================================================================
		

		HashMap map6 = new HashMap();
		//물리파일(자식)
		map6.put("RES_PATH", "/release/pmap/xml/");
		map6.put("RES_NAME", "AAUTB1006_PF218In.xml");
		
		//논리모델(부모)
		map6.put("PAR_RES_PATH", "/DBIO/");
		map6.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map6);
		//====================================================================
		
		
		//====================================================================
		
		HashMap map7 = new HashMap();
		//물리파일(자식)
		map7.put("RES_PATH", "/release/pmap/xml/");
		map7.put("RES_NAME", "AAUTB1006_PF218In.xml");
		
		//논리모델(부모)
		map7.put("PAR_RES_PATH", "/DBIO/");
		map7.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map7);
		//====================================================================
		
		HashMap map8 = new HashMap();
		//물리파일(자식)
		map8.put("RES_PATH", "/release/pmap/src/");
		map8.put("RES_NAME", "pfmDbioAAUTB1006_PF218Out.c");
		
		//논리모델(부모)
		map8.put("PAR_RES_PATH", "/DBIO/");
		map8.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map8);
		//====================================================================
		

		HashMap map9 = new HashMap();
		//물리파일(자식)
		map9.put("RES_PATH", "/release/pmap/inc/");
		map9.put("RES_NAME", "pfmDbioAAUTB1006_PF218Out.h");
		
		//논리모델(부모)
		map9.put("PAR_RES_PATH", "/DBIO/");
		map9.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map9);
		//====================================================================
		
		HashMap map10 = new HashMap();
		//물리파일(자식)
		map10.put("RES_PATH", "/release/pmap/xml/");
		map10.put("RES_NAME", "AAUTB1006_PF218Out.xml");
		
		//논리모델(부모)
		map10.put("PAR_RES_PATH", "/DBIO/");
		map10.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map10);
		//====================================================================
		
		//====================================================================
		
		HashMap map11 = new HashMap();
		//물리파일(자식)
		map11.put("RES_PATH", "/release/pmap/src/");
		map11.put("RES_NAME", "pfmDbioAAUTB1006_PF218Dyn.c");
		
		//논리모델(부모)
		map11.put("PAR_RES_PATH", "/DBIO/");
		map11.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map11);
		//====================================================================
		
		//====================================================================
		
		HashMap map12 = new HashMap();
		//물리파일(자식)
		map12.put("RES_PATH", "/release/pmap/inc/");
		map12.put("RES_NAME", "pfmDbioAAUTB1006_PF218Dyn.h");
		
		//논리모델(부모)
		map12.put("PAR_RES_PATH", "/DBIO/");
		map12.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map12);
		//====================================================================
		
		
		//====================================================================
		
		HashMap map13 = new HashMap();
		//물리파일(자식)
		map13.put("RES_PATH", "/release/pmap/xml/");
		map13.put("RES_NAME", "AAUTB1006_PF218Dyn.xml");
		
		//논리모델(부모)
		map13.put("PAR_RES_PATH", "/DBIO/");
		map13.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map13);
		//====================================================================
		
		
		//====================================================================
		
		HashMap map14 = new HashMap();
		//물리파일(자식)
		map14.put("RES_PATH", "/release/pmap/src/");
		map14.put("RES_NAME", "pfmDbioAAUTB1006_PF218BundleOut.c");
		
		//논리모델(부모)
		map14.put("PAR_RES_PATH", "/DBIO/");
		map14.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map14);
		//====================================================================
		
		//====================================================================
		
		HashMap map15 = new HashMap();
		//물리파일(자식)
		map15.put("RES_PATH", "/release/pmap/inc/");
		map15.put("RES_NAME", "pfmDbioAAUTB1006_PF218BundleOut.h");
		
		//논리모델(부모)
		map15.put("PAR_RES_PATH", "/DBIO/");
		map15.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map15);
		//====================================================================
		
		//====================================================================
		
		HashMap map16 = new HashMap();
		//물리파일(자식)
		map16.put("RES_PATH", "/release/pmap/xml/");
		map16.put("RES_NAME", "AAUTB1006_PF218BundleOut.xml");
		
		//논리모델(부모)
		map16.put("PAR_RES_PATH", "/DBIO/");
		map16.put("PAR_RES_NAME", "AAUTB1006_PF218");
		
		list.add(map16);
		//====================================================================	
		
		return list;
	}
         public List getLogicalNameListByServiceName(HashMap inHash)
	{
	}

}