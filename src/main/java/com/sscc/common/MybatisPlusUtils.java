package com.sscc.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MybatisPlusUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();
	// com.sscc.autoCode.modelName
	private static String modelName = null;

	private static String[] tables = { "btc_user", "found_blocks", "stats_pool_day", "stats_pool_hour",
			"stats_pool_minute", "stats_users_day", "stats_users_hour", "stats_users_minute", "stats_workers_day",
			"stats_workers_hour", "stats_workers_minute", "transfer_record" }; // 需要生成的表

	private static String packages = "com.btc.app";

	private static String commonResult = "com.btc.common.Result";

	private static String dataBaseURL = "jdbc:mysql://192.168.1.100/miner?characterEncoding=utf8&useSSL=false";
	private static String username = "root";
	private static String password = "sscc@123";
	private static String htmlProject = "E:\\web\\zcxy-admin-ui\\src\\components";
	private static boolean createHtml = false;

	public static void main(String[] args) {
		// String[] models = {"ssm-mapper","ssm-model","ssm-service","ssm-web"};

		shell("D:\\chenjian\\workspace\\btc\\btc-web");
	}

	private static void shell(String model) {
		File file = new File(model);
		String path = file.getAbsolutePath();
		System.out.println(path);
		// path = path.substring(0, path.lastIndexOf(File.separator));
		MyAutoGenerator mpg = new MyAutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(path + "/src/main/java");
		gc.setFileOverride(true);
		gc.setActiveRecord(false);
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(true);// XML columList
		gc.setAuthor("cc");

		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setTypeConvert(new MySqlTypeConvert() {
			// 自定义数据库表字段类型转换【可选】
			@Override
			public DbColumnType processTypeConvert(String fieldType) {
				// System.out.println("转换类型：" + fieldType);
				// 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
				return super.processTypeConvert(fieldType);
			}
		});
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername(username);
		dsc.setPassword(password);
		dsc.setUrl(dataBaseURL);
		mpg.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
		// strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });//
		// 此处可以修改为您的表前缀
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		strategy.setInclude(tables); // 需要生成的表
		strategy.setRestControllerStyle(true);
		// strategy.setExclude(new String[]{"test"}); // 排除生成的表
		// 自定义实体父类
		// strategy.setSuperEntityClass("com.spf.model.Entity");
		// 自定义实体，公共字段
		// strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
		// 自定义 mapper 父类
		// strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
		// 自定义 service 父类
		// strategy.setSuperServiceClass("com.baomidou.demo.TestService");
		// 自定义 service 实现类父类
		// strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
		// 自定义 controller 父类
		// strategy.setSuperControllerClass("com.baomidou.demo.TestController");
		// 【实体】是否生成字段常量（默认 false）
		// public static final String ID = "test_id";
		// strategy.setEntityColumnConstant(true);
		// 【实体】是否为构建者模型（默认 false）
		// public User setName(String name) {this.name = name; return this;}
		// strategy.setEntityBuliderModel(true);
		mpg.setStrategy(strategy);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(packages);
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setMapper("mapper");
		pc.setService("service");
		pc.setServiceImpl("service.impl");
		pc.setModuleName(modelName);
		mpg.setPackageInfo(pc);

		// 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
		InjectionConfigSupper cfg = new InjectionConfigSupper() {

			@Override
			public void initMap() {
				Map<String, Map<String, Object>> myMap = new HashMap<>();
				List<TableInfo> tableInfoList = this.getConfig().getTableInfoList();
				for (TableInfo tableInfo : tableInfoList) {
					Map<String, Object> map = new HashMap<String, Object>();
					myMap.put(tableInfo.getName(), map);

					try {
						System.err.println(objectMapper.writeValueAsString(tableInfo));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					getBaseColumnList(tableInfo, map);
					TableField pk = getTablePk(tableInfo);
					map.put("pk", pk);
					map.put("commonResult", commonResult);
				}

				this.setMyMap(myMap);
			}

			private TableField getTablePk(TableInfo tableInfo) {
				List<TableField> fields = tableInfo.getFields();
				for (TableField tableField : fields) {
					if (tableField.isKeyFlag()) {
						return tableField;
					}
				}
				return null;
			}

			private void getBaseColumnList(TableInfo tableInfo, Map<String, Object> map) {
				// TODO Auto-generated method stub
				StringBuilder columnNames = new StringBuilder();
				StringBuilder propertyNames = new StringBuilder();
				StringBuilder inserts = new StringBuilder();
				List<TableField> fields = tableInfo.getFields();
				for (int i = 0; i < fields.size(); i++) {
					TableField fd = fields.get(i);
					if (i == fields.size() - 1) {
						columnNames.append(fd.getName());
						propertyNames.append("#{").append(fd.getPropertyName()).append("}");
						inserts.append("#{").append("item.").append(fd.getPropertyName()).append("}");
					} else {
						columnNames.append(fd.getName()).append(", ");
						propertyNames.append("#{").append(fd.getPropertyName()).append("}").append(", ");
						inserts.append("#{").append("item.").append(fd.getPropertyName()).append("}").append(", ");
					}
				}
				map.put("baseColumnList", columnNames);
				map.put("basePropertyList", propertyNames);
				map.put("inserts", inserts);
			}
		};

		// 自定义 xxList.jsp 生成
		List<FileOutConfig> focList = new ArrayList<FileOutConfig>();

		focList.add(new FileOutConfig("/templates/mapper.xml.vm") {

			@Override
			public String outputFile(TableInfo tableInfo) {
				return path + "/src/main/resources/mapper/" + modelName + "/" + tableInfo.getEntityName()
						+ "Mapper.xml";
			}
		});
		if (createHtml) {
			focList.add(new FileOutConfig("/templates/list.vue.vm") {

				@Override
				public String outputFile(TableInfo tableInfo) {
					// TODO Auto-generated method stub
					return htmlProject + "\\" + modelName + "\\" + tableInfo.getEntityName() + "\\List.vue";
				}
			});

			focList.add(new FileOutConfig("/templates/edit.vue.vm") {

				@Override
				public String outputFile(TableInfo tableInfo) {
					// TODO Auto-generated method stub
					return htmlProject + "\\" + modelName + "\\" + tableInfo.getEntityName() + "\\Edit.vue";
				}
			});
			// focList.add(new FileOutConfig("/templates/add.vue.vm") {
			//
			// @Override
			// public String outputFile(TableInfo tableInfo) {
			// // TODO Auto-generated method stub
			// return htmlProject + "\\" + modelName + "\\" +
			// tableInfo.getEntityName() + "\\Add.vue";
			// }
			// });
			focList.add(new FileOutConfig("/templates/show.vue.vm") {

				@Override
				public String outputFile(TableInfo tableInfo) {
					// TODO Auto-generated method stub
					return htmlProject + "\\" + modelName + "\\" + tableInfo.getEntityName() + "\\Show.vue";
				}
			});

		}
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 关闭默认 xml 生成，调整生成 至 根目录
		TemplateConfig tc = new TemplateConfig();

		tc.setXml(null);
		mpg.setTemplate(tc);

		// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
		// 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
		// TemplateConfig tc = new TemplateConfig();
		// tc.setController("...");
		// tc.setEntity("...");
		// tc.setMapper("...");
		// tc.setXml("...");
		// tc.setService("...");
		// tc.setServiceImpl("...");
		// 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
		// mpg.setTemplate(tc);

		// 执行生成
		mpg.execute();

		// 打印注入设置【可无】
	}
}
