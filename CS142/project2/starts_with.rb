# if the first letters of the string in the array matches the matchStr
# passed in, yields to the block
def each_starts_with (arr,matchStr)
  arr.each do |str|
    i = str[0, matchStr.length] <=> matchStr
    if i==0
      yield str
    end
  end
end

each_starts_with(["abcd", "axyz", "able", "xyzab", "qrst"], "ab") do |s|
  puts(s)
end

each_starts_with(["spal", "space", "saps", "eisp", "speech"], "sp") do |s|
  puts(s)
end

each_starts_with(["spal", "niceapp", "nipe", "nicer", "nice"], "nice") do |s|
  puts(s)
end
