/**
 * Removes white spaces from the begin and end of a long string.
 * @param str
 * @returns
 */
function trimLong (str)
{	
	str.value = str.value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	return;
}