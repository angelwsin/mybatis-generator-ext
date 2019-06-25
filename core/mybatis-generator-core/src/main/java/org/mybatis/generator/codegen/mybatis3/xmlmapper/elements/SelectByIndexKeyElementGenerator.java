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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class SelectByIndexKeyElementGenerator  extends
        AbstractXmlElementGenerator {

    public SelectByIndexKeyElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        for(String key : introspectedTable.getIndexes().keySet()){

            XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
            answer.addAttribute(new Attribute(
                    "id", introspectedTable.getSelectByIndexKeyStatementId(key))); //$NON-NLS-1$
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));

            context.getCommentGenerator().addComment(answer);

            StringBuilder sb = new StringBuilder();
            sb.append("select "); //$NON-NLS-1$

            answer.addElement(new TextElement(sb.toString()));
            answer.addElement(getBaseColumnListElement());
            if (introspectedTable.hasBLOBColumns()) {
                answer.addElement(new TextElement(",")); //$NON-NLS-1$
                answer.addElement(getBlobColumnListElement());
            }

            sb.setLength(0);
            sb.append("from "); //$NON-NLS-1$
            sb.append(introspectedTable
                    .getAliasedFullyQualifiedTableNameAtRuntime());
            answer.addElement(new TextElement(sb.toString()));

            boolean and = false;
            for (IntrospectedColumn introspectedColumn : introspectedTable.getIndexes().get(key)) {
                sb.setLength(0);
                if (and) {
                    sb.append("  and ");
                } else {
                    sb.append("where "); //$NON-NLS-1$
                    and = true;
                }

                sb.append(MyBatis3FormattingUtilities
                        .getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" = "); //$NON-NLS-1$
                sb.append(MyBatis3FormattingUtilities
                        .getParameterClause(introspectedColumn));
                answer.addElement(new TextElement(sb.toString()));
            }
            parentElement.addElement(answer);

        }

    }
}
