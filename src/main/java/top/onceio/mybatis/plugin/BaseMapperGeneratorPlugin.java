package top.onceio.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class BaseMapperGeneratorPlugin extends PluginAdapter {

	public boolean validate(List<String> warnings) {
		return true;
	}

	/**
	 * 生成dao
	 */
	//@Override
	public boolean __clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		/**
		 * 主键默认采用java.lang.Integer
		 */
		FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("BaseMapper<"
				+ introspectedTable.getBaseRecordType() + ","
				+ introspectedTable.getExampleType() + ","
				+ "java.lang.Long" + ">");
		FullyQualifiedJavaType imp = new FullyQualifiedJavaType(
				"BaseMapper");
		/**
		 * 添加 extends MybatisBaseMapper
		 */
		interfaze.addSuperInterface(fqjt);

		/**
		 * 添加import my.mabatis.example.base.MybatisBaseMapper;
		 */
		interfaze.addImportedType(imp);
		/**
		 * 方法不需要
		 */
		interfaze.getMethods().clear();
		interfaze.getAnnotations().clear();
		return true;
	}
	
	@Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {

        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance()
                .getPrimitiveTypeWrapper();

        Field limit = new Field();
        limit.setName("limit");
        limit.setVisibility(JavaVisibility.PRIVATE);
        limit.setType(integerWrapper);
        topLevelClass.addField(limit);

        Method setLimit = new Method();
        setLimit.setVisibility(JavaVisibility.PUBLIC);
        setLimit.setName("setLimit");
        setLimit.addParameter(new Parameter(integerWrapper, "limit"));
        setLimit.addBodyLine("this.limit = limit;");
        topLevelClass.addMethod(setLimit);

        Method getLimit = new Method();
        getLimit.setVisibility(JavaVisibility.PUBLIC);
        getLimit.setReturnType(integerWrapper);
        getLimit.setName("getLimit");
        getLimit.addBodyLine("return limit;");
        topLevelClass.addMethod(getLimit);

        Field offset = new Field();
        offset.setName("offset");
        offset.setVisibility(JavaVisibility.PRIVATE);
        offset.setType(integerWrapper);
        topLevelClass.addField(offset);

        Method setOffset = new Method();
        setOffset.setVisibility(JavaVisibility.PUBLIC);
        setOffset.setName("setOffset");
        setOffset.addParameter(new Parameter(integerWrapper, "offset"));
        setOffset.addBodyLine("this.offset = offset;");
        topLevelClass.addMethod(setOffset);

        Method getOffset = new Method();
        getOffset.setVisibility(JavaVisibility.PUBLIC);
        getOffset.setReturnType(integerWrapper);
        getOffset.setName("getOffset");
        getOffset.addBodyLine("return offset;");
        topLevelClass.addMethod(getOffset);

        return true;
    }

    /**
     * 为 Mapper.xml 的 selectByExample 添加 limit     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        addPageElement(element);
        return true;
    }

    /**
     * 为 Mapper.xml 的 selectByExampleWithBLOBs 添加 limit     */
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
                                                                  IntrospectedTable introspectedTable) {
        addPageElement(element);
        return true;
    }

    private void addPageElement(XmlElement element) {
        XmlElement ifLimitNotNullElement = new XmlElement("if");
        ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));

        XmlElement ifOffsetNotNullElement = new XmlElement("if");
        ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
        ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${limit}"));
        ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

        XmlElement ifOffsetNullElement = new XmlElement("if");
        ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
        ifOffsetNullElement.addElement(new TextElement("limit ${limit}"));
        ifLimitNotNullElement.addElement(ifOffsetNullElement);

        element.addElement(ifLimitNotNullElement);
    }



	/**
	 * {@inheritDoc}
	 */
    /*
	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {

			interfaze.addMethod(generateDeleteLogicByIds(method,
					introspectedTable));

		return true;//super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
*/
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {

		interfaze.addMethod(generateBatchInsert(method, introspectedTable));
		interfaze.addMethod(generateBatchUpdate(method, introspectedTable));
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		
			topLevelClass.addMethod(generateDeleteLogicByIds(method,
					introspectedTable));
		return true;//super.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		topLevelClass.addMethod(generateBatchInsert(method, introspectedTable));
		topLevelClass.addMethod(generateBatchUpdate(method, introspectedTable));
		return true;
	}

	private XmlElement createBatchInsert(String tableName, List<IntrospectedColumn> columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into " + tableName+"\n    (");
		for(IntrospectedColumn col:columns) {
			sb.append(col.getActualColumnName() + ", ");
		}
		sb.delete(sb.length()-2,sb.length());
		sb.append(")\n    values \n");

		sb.append("    <foreach item=\"item\" index=\"index\" collection=\"list\" separator=\",\">\n");
		sb.append("        (");
		for(IntrospectedColumn col:columns) {
			sb.append("#{item."+col.getActualColumnName()+",jdbcType="+col.getJdbcTypeName()+"}, ");
		}
		sb.delete(sb.length()-2,sb.length());
		sb.append(")\n    </foreach>");
		
		// 产生分页语句前半部分
		XmlElement batchInsert = new XmlElement("insert");
		batchInsert.addAttribute(new Attribute("id", "batchInsert"));
		batchInsert.addElement(new TextElement("<!--\n" + 
				"      WARNING - @mbggenerated\n" + 
				"      This element is automatically generated by MyBatis Generator, do not modify.\n" + 
				"    -->"));
		batchInsert.addElement(new TextElement(sb.toString()));
		return batchInsert;
	}
	
	private XmlElement createBatchUpdate(String tableName, List<IntrospectedColumn> primaryKeys, List<IntrospectedColumn> columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("<foreach item=\"item\" index=\"index\" collection=\"list\" separator=\";\">\n");
		sb.append("      update " + tableName+" set \n");
		sb.append("        <trim suffixOverrides=\",\">\n");
		for(IntrospectedColumn col:columns) {
			if(!col.isIdentity()) {
				sb.append("         <if test=\"item."+col.getActualColumnName()+" != null\">");
				sb.append(col.getActualColumnName() + " = ");
				sb.append("#{item."+col.getActualColumnName()+",jdbcType="+col.getJdbcTypeName()+"},");
				sb.append("</if>\n");
			}
		}
		sb.append("        </trim>\n");
		if(primaryKeys != null && !primaryKeys.isEmpty()) {
			sb.append("        where ");
			for(IntrospectedColumn primary:primaryKeys) {
				sb.append(primary.getActualColumnName() +" = #{item."+primary.getActualColumnName()+",jdbcType="+primary.getJdbcTypeName()+"} and ");	
			}
			sb.delete(sb.length()-5, sb.length());
			sb.append("\n");
		}
		sb.append("    </foreach>");
		
		// 产生分页语句前半部分
		XmlElement batchUpdate = new XmlElement("update");
		batchUpdate.addAttribute(new Attribute("id", "batchUpdate"));
		batchUpdate.addElement(new TextElement("<!--\n" + 
				"      WARNING - @mbggenerated\n" + 
				"      This element is automatically generated by MyBatis Generator, do not modify.\n" + 
				"    -->"));
		batchUpdate.addElement(new TextElement(sb.toString()));
		return batchUpdate;
	}
	
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		
		String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名  
		List<IntrospectedColumn> primaryKeys = introspectedTable.getPrimaryKeyColumns();
		List<IntrospectedColumn> columns =introspectedTable.getAllColumns();
		XmlElement parentElement = document.getRootElement();
		XmlElement batchInsert = createBatchInsert(tableName,columns);
		parentElement.addElement(batchInsert);
		XmlElement batchUpdate = createBatchUpdate(tableName,primaryKeys,columns);
		parentElement.addElement(batchUpdate);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	private Method generateBatchInsert(Method method, IntrospectedTable introspectedTable) {
		String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名  
		Method m = new Method();
		m.setName("batchInsert");
		m.setVisibility(method.getVisibility());
		m.setReturnType(FullyQualifiedJavaType.getIntInstance());
		
		m.addJavaDocLine("/**\n" + 
				"     * This method was generated by MyBatis Generator.\n" + 
				"     * This method corresponds to the database table " +tableName + "\n"+ 
				"     *\n" + 
				"     * @mbggenerated\n" + 
				"     */");
		
		m.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+introspectedTable.getBaseRecordType()+">"), "list", "@Param(\"list\")"));
		return m;
	}
    
	private Method generateBatchUpdate(Method method, IntrospectedTable introspectedTable) {
		String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名  
		Method m = new Method();
		m.setName("batchUpdate");
		m.setVisibility(method.getVisibility());
		m.setReturnType(FullyQualifiedJavaType.getIntInstance());
		
		m.addJavaDocLine("/**\n" + 
				"     * This method was generated by MyBatis Generator.\n" + 
				"     * This method corresponds to the database table " +tableName + "\n"+ 
				"     *\n" + 
				"     * @mbggenerated\n" + 
				"     */");
		
		m.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+introspectedTable.getBaseRecordType()+">"), "list", "@Param(\"list\")"));
		return m;
	}

}