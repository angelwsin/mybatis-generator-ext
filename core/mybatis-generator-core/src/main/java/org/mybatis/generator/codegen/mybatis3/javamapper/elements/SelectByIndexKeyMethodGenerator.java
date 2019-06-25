/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SelectByIndexKeyMethodGenerator extends AbstractJavaMapperMethodGenerator {

    @Override
    public void addInterfaceElements(Interface interfaze) {

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        for(String key : introspectedTable.getIndexes().keySet()){
            Method method = new Method(introspectedTable.getSelectByIndexKeyStatementId(key));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setAbstract(true);

            FullyQualifiedJavaType returnType = introspectedTable.getRules()
                    .calculateAllFieldsClass();
            method.setReturnType(returnType);
            importedTypes.add(returnType);
            // no primary key class - fields are in the base class
            // if more than one PK field, then we need to annotate the
            // parameters
            // for MyBatis3
            List<IntrospectedColumn> introspectedColumns = introspectedTable
                    .getPrimaryKeyColumns();
            boolean annotate = introspectedColumns.size() > 1;
            if (annotate) {
                importedTypes.add(new FullyQualifiedJavaType(
                        "org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
            }
            StringBuilder sb = new StringBuilder();
            for (IntrospectedColumn introspectedColumn : introspectedTable.getIndexes().get(key)) {
                FullyQualifiedJavaType type = introspectedColumn
                        .getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn
                        .getJavaProperty());
                if (annotate) {
                    sb.setLength(0);
                    sb.append("@Param(\""); //$NON-NLS-1$
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append("\")"); //$NON-NLS-1$
                    parameter.addAnnotation(sb.toString());
                }
                method.addParameter(parameter);
            }
            addMapperAnnotations(interfaze, method);

            context.getCommentGenerator().addGeneralMethodComment(method,
                    introspectedTable);
            interfaze.addMethod(method);

        }
        addExtraImports(interfaze);
        interfaze.addImportedTypes(importedTypes);

        /*if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(
                method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }*/
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {

    }
}
