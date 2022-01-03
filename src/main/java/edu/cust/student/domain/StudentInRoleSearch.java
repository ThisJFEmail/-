package edu.cust.student.domain;

import edu.cust.util.ListTemplate;
import edu.cust.util.search.Search;
import edu.cust.util.ListTemplate;
import edu.cust.util.search.Search;

public class StudentInRoleSearch extends Search {
    @Override
    protected String getTables(ListTemplate lt) {
        return "(select a.* from c_student a left outer join (select c_number from c_student_role where c_role_id=?) b on a.c_number=b.c_number where b.c_number is not null) c";
    }
}


