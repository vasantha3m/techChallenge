package model

import lombok.Generated

@Generated
case class Bikes(
  date_stolen:       String,
  description:       String,
  frame_colors:      Array[String],
  frame_model:       String,
  id:                String,
  is_stock_img:      Boolean,
  large_img:         String,
  location_found:    String,
  manufacturer_name: String,
  external_id:       String,
  registry_name:     String,
  registry_url:      String,
  serial:            String,
  status:            String,
  stolen:            Boolean,
  stolen_location:   String,
  thumb:             String,
  title:             String,
  url:               String,
  year:              String)

@Generated
case class Result(bikes: Array[Bikes])