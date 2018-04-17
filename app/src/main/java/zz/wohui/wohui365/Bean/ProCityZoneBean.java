package zz.wohui.wohui365.Bean;

import java.util.List;

/**
 * 封装全国省市县三级目录的Bean
 * @author 陈杰宇
 * 
 * DATE:2015-9-28
 *
 */
public class ProCityZoneBean {

	public List<Datas> data;

	public class Datas {
		public String p;
		public List<CCC> c;
	}

	public class CCC {
		public String cc;
		public List<ZoneD> d;
	}

	public class ZoneD {
		public String dd;
	}

}
