package schemas

object HBaseBikeSchema {
  def tableSchema =
    s"""{
     |"table":{"name":"bikes"},
     |"rowkey":"date_stolen:id",
     |"columns":{
     |"date_stolen":{"cf":"stolen_info","col":"date_stolen","type":"string"},
	   |"description":{"cf":"stolen_info","col":"description","type":"string"},
	   |"location_found":{"cf":"stolen_info","col":"location_found","type":"string"},
     |"stolen_location":{"cf":"stolen_info","col":"stolen_location","type":"string"},
	   |"id":{"cf":"bike_info","col":"id","type":"string"},
     |"serial":{"cf":"bike_info","col":"serial","type":"string"},
	   |"title":{"cf":"bike_info","col":"title","type":"string"},
     |"frame_colors":{"cf":"bike_info","col":"frame_colors","type":"Array"},
	   |"frame_model":{"cf":"bike_info","col":"frame_model","type":"string"},
	   |"manufacturer_name":{"cf":"bike_info","col":"manufacturer_name","type":"string"},
	   |"year":{"cf":"bike_info","col":"year","type":"string"},
	   |"url":{"cf":"bike_info","col":"url","type":"string"},
	   |"status":{"cf":"bike_info","col":"status","type":"string"},
	   |"stolen":{"cf":"bike_info","col":"stolen","type":"string"},
	   |"is_stock_img":{"cf":"other_info","col":"is_stock_img","type":"string"},
	   |"large_img":{"cf":"other_info","col":"large_img","type":"string"},
	   |"external_id":{"cf":"other_info","col":"external_id","type":"string"},
     |"registry_name":{"cf":"other_info","col":"registry_name","type":"string"},
	   |"registry_url":{"cf":"other_info","col":"registry_url","type":"string"},
	   |"thumb":{"cf":"other_info","col":"thumb","type":"string"}kkkjkjjj
     |}
     |}""".stripMargin
}