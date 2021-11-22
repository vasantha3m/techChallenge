package util

import scala.collection.mutable

class UrlBuilder {

  def buildUrl(props: mutable.Map[String, String]): String = {
    val urlTemplate = props.get("URL_TEMPLATE").get
    val urlType = props.get("URL_TYPE").get
    val pageNo = props.get("PAGE_NO").get
    val perPage = props.get("PER_PAGE").get
    val stoleness = props.get("STOLENESS").get
    val accessToken = props.get("ACCESS_TOKEN").get
    val urlBuilt: String = {
      if ("summary".equalsIgnoreCase(urlType)) {
        urlTemplate
      } else {
        var apiUrl = urlTemplate
        if (checkForAnyUrlOption(pageNo, stoleness, accessToken)) {
          apiUrl = apiUrl.concat("?")
          if (checkIfValueExists(pageNo))
            apiUrl = apiUrl.concat("page=" + pageNo + "&per_page=" + perPage + "&")
          if (checkIfValueExists(stoleness))
            apiUrl = apiUrl.concat("stolenness=" + stoleness + "&")
          if (checkIfValueExists(accessToken))
            apiUrl = apiUrl.concat("access_token=" + accessToken + "&")
          apiUrl.substring(0, apiUrl.length() - 1)
        } else
          urlTemplate
      }
    }
    urlBuilt
  }

  def checkForAnyUrlOption(pageNo: String, stoleness: String, accessToken: String): Boolean = {
    if (checkIfValueExists(pageNo))
      true
    else if (checkIfValueExists(stoleness))
      true
    else if (checkIfValueExists(accessToken))
      true
    else
      false
  }

  def checkIfValueExists(value: String): Boolean = {
    if (value != null && value.length() > 0)
      true
    else
      false
  }
}