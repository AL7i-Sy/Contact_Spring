package app;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ContactRowMapper implements RowMapper<Contact> {

	@Override
	public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
		Contact c = new Contact();
		c.setId(rs.getInt("id"));
		c.setEmail(rs.getString("email"));
		c.setAddress(rs.getString("address"));
		c.setFirstname(rs.getString("firstname"));
		c.setLastname(rs.getString("lastname"));
		c.setPhone(rs.getString("phone"));
		return c;
	}

}
