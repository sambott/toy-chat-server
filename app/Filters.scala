import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 20/04/2016.
  */

class Filters @Inject() (corsFilter: CORSFilter) extends HttpFilters {
  def filters = Seq(corsFilter)
}
